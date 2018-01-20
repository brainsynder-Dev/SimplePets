package simplepets.brainsynder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simple.brainsynder.utils.ObjectPager;
import simple.brainsynder.utils.Reflection;
import simple.brainsynder.utils.ServerVersion;
import simple.brainsynder.utils.SpigotPluginHandler;
import simplepets.brainsynder.commands.CMD_Pet;
import simplepets.brainsynder.database.ConnectionPool;
import simplepets.brainsynder.database.MySQL;
import simplepets.brainsynder.events.*;
import simplepets.brainsynder.files.*;
import simplepets.brainsynder.links.IProtectionLink;
import simplepets.brainsynder.links.impl.WorldGuardLink;
import simplepets.brainsynder.menu.ItemStorageMenu;
import simplepets.brainsynder.nms.VersionNMS;
import simplepets.brainsynder.nms.entities.v1_8_R3.SpawnUtil;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.utils.ISpawner;
import simplepets.brainsynder.utils.LoaderRetriever;

import java.io.File;
import java.sql.Connection;
import java.util.*;

public class PetCore extends JavaPlugin {
    private static PetCore instance;
    private final List<String> supportedVersions = Arrays.asList(
            "v1_8_R3",
            "v1_9_R1",
            "v1_9_R2",
            "v1_10_R1",
            "v1_11_R1",
            "v1_12_R1"
    );
    public boolean forceSpawn;
    public ObjectPager<PetType> petTypes;
    @Getter private boolean disabling = false;
    @Getter private Config configuration;
    @Getter private Messages messages;
    @Getter @Setter(value = AccessLevel.PRIVATE) private MySQL mySQL = null;
    @Getter private IProtectionLink worldGuardLink;
    private ISpawner spawner;
    @Getter @Setter private IStorage<Integer> availableSlots = new StorageList<>();
    private Map<UUID, PlayerFile> fileStorage = new HashMap<>();

    public static PetCore get() {
        return instance;
    }

    public static boolean hasPerm(Player p, String perm) {
        if (get().configuration.getBoolean("Needs-Permission")) {
            if (!p.hasPermission(perm)) {
                return false;
            }
        }
        return true;
    }

    public void onEnable() {
        long start = System.currentTimeMillis();
        Plugin plugin = getServer().getPluginManager().getPlugin("SimpleAPI");
        if (plugin == null) {
            System.out.println("SimplePets >> Missing dependency (SimpleAPI) Must have the plugin in order to work the plugin");
            new MissingAPI(this).runTaskTimer(this, 0, 20 * 60 * 2);
            return;
        }
        double ver = Double.parseDouble(plugin.getDescription().getVersion());
        if (ver < 3.8) {
            System.out.println("SimplePets >> Notice: Your Version of SimpleAPI is OutOfDate, Please update SimpleAPI https://www.spigotmc.org/resources/24671/");
            System.out.println("Disabling SimplePets...");
            setEnabled(false);
            return;
        }
        SpigotPluginHandler spigotPluginHandler = new SpigotPluginHandler(this, 14124, SpigotPluginHandler.MetricType.BSTATS);
        SpigotPluginHandler.registerPlugin(spigotPluginHandler);
        if (!spigotPluginHandler.runTamperCheck("brainsynder", "SimplePets", "3.9")) {
            setEnabled(false);
            return;
        }
        try {
            Class.forName("org.spigotmc.event.entity.EntityMountEvent");
        } catch (Exception e) {
            System.out.println("Please ensure you are using a version of Spigot. Either PaperSpigot, TacoSpigot, Spigot, or any other Spigot Software");
            System.out.println("SimplePets requires events in the Spigot Software that CraftBukkit does not offer.");
            System.out.println("Disabling SimplePets...");
            setEnabled(false);
            return;
        }

        double version = getJavaVersion();
        if (version == 0.0) {
            System.out.println("An error occurred when trying to get the simplified Java version for: '" + System.getProperty("java.version") + "' Please make sure you are using a recommended Java version (Java 8)");
        } else {
            if (!(version >= 1.8)) {
                System.out.println("-------------------------------------------");
                System.out.println("          Error Type: CRITICAL");
                System.out.println("    An Internal Version Error Occurred");
                System.out.println("SimplePets Requires Java 8+ in order to work. Please update Java.");
                System.out.println("-------------------------------------------");
                setEnabled(false);
                return;
            }
        }

        instance = this;
        saveResource("SimplePets-Info-App.txt", true);
        if (!supportedVersions.contains(Reflection.getVersion())) {
            System.out.println("-------------------------------------------");
            System.out.println("          Error Type: CRITICAL");
            System.out.println("    An Internal Version Error Occurred");
            System.out.println("SimplePets Does not support " + Reflection.getVersion() + ", Please Update your server.");
            System.out.println("-------------------------------------------");
            setEnabled(false);
            return;
        }
        if (!Reflection.getVersion().equals("v1_12_R1")) {
            System.out.println("-------------------------------------------");
            System.out.println("          Error Type: WARNING");
            System.out.println("You seem to be on a version below 1.12.1");
            System.out.println("SimplePets works best on 1.12.1, Just saying :P");
            System.out.println("-------------------------------------------");
        }
        loadConfig();
        LoaderRetriever.initiate();
        VersionNMS.registerPets();
        PetType.initiate();
        debug("Registering Listeners...");
        getCommand("pet").setExecutor(new CMD_Pet());
        getServer().getPluginManager().registerEvents(new MainListeners(), this);
        getServer().getPluginManager().registerEvents(new OnJoin(), this);
        getServer().getPluginManager().registerEvents(new ItemStorageMenu(), this);
        getServer().getPluginManager().registerEvents(new PetEventListeners(), this);
        getServer().getPluginManager().registerEvents(new DataListener(), this);
        getServer().getPluginManager().registerEvents(new OnPetSpawn(), this);
        getServer().getPluginManager().registerEvents(new PetSelectionMenu(), this);
        int v = ServerVersion.getVersion().getIntVersion();
        if ((v < 18) || (ServerVersion.getVersion() == ServerVersion.UNKNOWN)) {
            PetCore.get().debug("This version is not supported, be sure you are between 1.8.8 and 1.12");
            setEnabled(false);
            return;
        }
        debug("Loading PetMenu Layout");
        List<String> _allowed_ = configuration.getStringList("AvailableSlots");
        int size = 1;
        for (String s : _allowed_) {
            try {
                int slot = Integer.parseInt(s);
                if (slot <= 0) {
                    PetCore.get().debug("SimplePets Error: Invalid Slot number '" + slot + "' Value must be from 1-54");
                    continue;
                }
                if (slot >= 55) {
                    PetCore.get().debug("SimplePets Error: Invalid Slot number '" + slot + "' Value must be from 1-54");
                    continue;
                }
                availableSlots.add((slot - 1));
            } catch (NumberFormatException e) {
                PetCore.get().debug("SimplePets Error: Invalid Slot number '" + s + "' Value must be from 1-54");
            }
            size++;
        }

        List<PetType> types = new ArrayList<>();
        for (PetType type : PetType.values()) {
            if (type.isSupported()) {
                if (type.isEnabled()) {
                    types.add(type);
                }
            }
        }
        petTypes = new ObjectPager<>(size, types);
        worldGuardLink = new WorldGuardLink();
        if (getConfiguration().isSet("MySQL.Enabled")) handleSQL();
        debug("Took " + (System.currentTimeMillis() - start) + "ms to load");
    }

    public ISpawner getSpawner() {
        if (spawner == null) {
            reloadSpawner();
        }
        return spawner;
    }

    private double getJavaVersion() {
        try {
            String version = System.getProperty("java.version");
            int pos = version.indexOf('.');
            pos = version.indexOf('.', pos + 1);
            return Double.parseDouble(version.substring(0, pos));
        } catch (Throwable t) {
            return 0.0;
        }
    }

    private void handleSQL() {
        if (getConfiguration().getBoolean("MySQL.Enabled")) {
            String host = getConfiguration().getString("MySQL.Host", false);
            String port = getConfiguration().getString("MySQL.Port", false);
            String databaseName = getConfiguration().getString("MySQL.DatabaseName", false);
            String username = getConfiguration().getString("MySQL.Login.Username", false);
            String password = getConfiguration().getString("MySQL.Login.Password", false);
            mySQL = new MySQL(host, port, databaseName, username, password);
            Thread thread = new Thread(() -> {
                try {
                    debug("Creating SQL table if there is none...");
                    ConnectionPool pool = mySQL.getPool();
                    Connection connection = pool.borrowConnection();
                    connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `SimplePets` (`UUID` TEXT,`name` TEXT,`UnlockedPets` MEDIUMTEXT,`PetName` TEXT,`NeedsRespawn` MEDIUMTEXT);");
                    pool.surrenderConnection(connection);
                    connection.close();
                } catch (Exception e) {
                    debug("Unable to create default SQL tables Error:");
                    e.printStackTrace();
                }
            });
            thread.setName("SimplePets SQL");
            thread.setDaemon(false);
            thread.start();
        }
    }

    private void reloadSpawner() {
        ServerVersion version = ServerVersion.getVersion();
        if (version == ServerVersion.v1_8_R3) {
            spawner = new SpawnUtil();
            debug("Successfully Linked to v1_8_R3 SpawnUtil Class");
        } else if (version == ServerVersion.v1_9_R1) {
            spawner = new simplepets.brainsynder.nms.entities.v1_9_R1.SpawnUtil();
            debug("Successfully Linked to v1_9_R1 SpawnUtil Class");
        } else if (version == ServerVersion.v1_9_R2) {
            spawner = new simplepets.brainsynder.nms.entities.v1_9_R2.SpawnUtil();
            debug("Successfully Linked to v1_9_R2 SpawnUtil Class");
        } else if (version == ServerVersion.v1_10_R1) {
            spawner = new simplepets.brainsynder.nms.entities.v1_10_R1.SpawnUtil();
            debug("Successfully Linked to v1_10_R1 SpawnUtil Class");
        } else if (version == ServerVersion.v1_11_R1) {
            spawner = new simplepets.brainsynder.nms.entities.v1_11_R1.SpawnUtil();
            debug("Successfully Linked to v1_11_R1 SpawnUtil Class");
        } else if (version == ServerVersion.v1_12_R1) {
            spawner = new simplepets.brainsynder.nms.entities.v1_12_R1.SpawnUtil();
            debug("Successfully Linked to v1_12_R1 SpawnUtil Class");
        } else {
            spawner = null;
            debug("Could not link to a SpawnUtil Class... Possible Wrong version?");
        }
    }

    public String getDefaultPetName(PetType petType, Player player) {
        String name = petType.getDefaultName();
        return translateName(name).replace("%player%", player.getName());
    }

    public String translateName(String name) {
        boolean color = get().configuration.getBoolean("ColorCodes");
        boolean k = get().configuration.getBoolean("Use&k");
        if (color)
            name = ChatColor.translateAlternateColorCodes('&', k ? name : name.replace("&k", "k"));

        return name;
    }

    public PlayerFile getPlayerFile(Player player) {
        if (fileStorage.containsKey(player.getUniqueId()))
            return fileStorage.get(player.getUniqueId());
        PlayerFile file = new PlayerFile(player);
        fileStorage.put(player.getUniqueId(), file);
        return fileStorage.get(player.getUniqueId());
    }

    public PlayerPetInv getPlayerPetInv(Player player) {
        return new PlayerPetInv(player.getUniqueId() + ".storage");
    }

    public PlayerPetInv getPetInvByName(String name) {
        File folder = new File(getDataFolder().toString() + "/PetInventories/");
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                if (files.length != 0) {
                    for (File file : files) {
                        if (file.getName().contains(".storage")) {
                            FileConfiguration con = YamlConfiguration.loadConfiguration(file);
                            if (con.get("Username") != null) {
                                if (con.getString("Username").equalsIgnoreCase(name)) {
                                    return new PlayerPetInv(file.getName());
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void loadConfig() {
        debug("Loading Config.yml...");
        configuration = new Config(this, "Config.yml");
        configuration.loadDefaults();
        debug("Loading Messages.yml...");
        messages = new Messages(this, "Messages.yml");
        messages.loadDefaults();
        debug("Loading PetTranslator.yml... (Longest Task)");
        PetTranslate.loadDefaults();
    }

    public void onDisable() {
        disabling = true;
        for (Player player : Bukkit.getOnlinePlayers()) {
            PetOwner petOwner = PetOwner.getPetOwner(player);
            if (petOwner.hasPet()) {
                petOwner.removePet();
            }
            petOwner.getFile().save();
        }

        if (getConfiguration().getBoolean("MySQL.Enabled")) {
            if (mySQL != null) {
                mySQL.getPool().dumpPool();
                mySQL = null;
            }
        }
        VersionNMS.unregisterPets();
        try {
            Thread.sleep(20L);
        } catch (InterruptedException ignored) {
        }
    }

    public void debug(String message) {
        debug(0, message);
    }

    public void debug(int level, String message) {
        if (level >= 3) level = 2;
        if (configuration == null) {
            ChatColor color = ChatColor.WHITE;
            switch (level) {
                case 1:
                    color = ChatColor.YELLOW;
                    break;
                case 2:
                    color = ChatColor.RED;
                    break;
            }
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[SimplePets Debug] " + color + message);
            return;
        }
        if (!configuration.isSet("Debug.Enabled")) {
            ChatColor color = ChatColor.WHITE;
            switch (level) {
                case 1:
                    color = ChatColor.YELLOW;
                    break;
                case 2:
                    color = ChatColor.RED;
                    break;
            }
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[SimplePets Debug] " + color + message);
            return;
        }
        if (!configuration.getBoolean("Debug.Enabled")) return;
        if (!configuration.getStringList("Debug.Levels").contains(String.valueOf(level))) return;
        ChatColor color = ChatColor.WHITE;
        switch (level) {
            case 1:
                color = ChatColor.YELLOW;
                break;
            case 2:
                color = ChatColor.RED;
                break;
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[SimplePets Debug] " + color + message);
    }

    public void reload () {
        if (getConfiguration().isSet("MySQL.Enabled")) {
            String host = getConfiguration().getString("MySQL.Host", false);
            String port = getConfiguration().getString("MySQL.Port", false);
            String databaseName = getConfiguration().getString("MySQL.DatabaseName", false);
            String username = getConfiguration().getString("MySQL.Login.Username", false);
            String password = getConfiguration().getString("MySQL.Login.Password", false);
            mySQL = new MySQL(host, port, databaseName, username, password);
            Thread thread = new Thread(() -> {
                try {
                    debug("Creating SQL table if there is none...");
                    ConnectionPool pool = mySQL.getPool();
                    Connection connection = pool.borrowConnection();
                    connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `SimplePets` (`UUID` TEXT,`name` TEXT,`UnlockedPets` MEDIUMTEXT,`PetName` TEXT,`NeedsRespawn` MEDIUMTEXT);");
                    pool.surrenderConnection(connection);
                    connection.close();
                } catch (Exception e) {
                    debug("Unable to create default SQL tables Error:");
                    e.printStackTrace();
                }
            });
            thread.setName("SimplePets SQL");
            thread.setDaemon(false);
            thread.start();
        }
    }
}
