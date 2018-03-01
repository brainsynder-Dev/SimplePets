package simplepets.brainsynder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import simple.brainsynder.utils.Reflection;
import simple.brainsynder.utils.ServerVersion;
import simple.brainsynder.utils.SpigotPluginHandler;
import simplepets.brainsynder.commands.CMD_Pet;
import simplepets.brainsynder.database.ConnectionPool;
import simplepets.brainsynder.database.MySQL;
import simplepets.brainsynder.links.LinkRetriever;
import simplepets.brainsynder.listeners.MainListeners;
import simplepets.brainsynder.listeners.OnJoin;
import simplepets.brainsynder.listeners.OnPetSpawn;
import simplepets.brainsynder.listeners.PetEventListeners;
import simplepets.brainsynder.menu.ItemStorageMenu;
import simplepets.brainsynder.menu.inventory.InvLoaders;
import simplepets.brainsynder.menu.inventory.listeners.DataListener;
import simplepets.brainsynder.menu.inventory.listeners.SelectionListener;
import simplepets.brainsynder.menu.items.ItemLoaders;
import simplepets.brainsynder.nms.VersionNMS;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.files.Config;
import simplepets.brainsynder.storage.files.Messages;
import simplepets.brainsynder.storage.files.PetTranslator;
import simplepets.brainsynder.storage.files.PlayerStorage;
import simplepets.brainsynder.utils.Errors;
import simplepets.brainsynder.utils.ISpawner;
import simplepets.brainsynder.utils.Utilities;

import java.io.File;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PetCore extends JavaPlugin {

    private static PetCore instance;
    private final List<String> supportedVersions = Arrays.asList(
            "v1_11_R1",
            "v1_12_R1"
    );
    public boolean forceSpawn;
    private boolean disabling = false;

    private ItemLoaders itemLoaders;
    private InvLoaders invLoaders;
    private Config configuration;
    private Messages messages;
    private PetTranslator translator;
    private Utilities utilities = null;
    private MySQL mySQL = null;
    private CMD_Pet cmd_pet;
    private LinkRetriever linkRetriever;

    private ISpawner spawner;
    private Map<UUID, PlayerStorage> fileStorage = new HashMap<>();

    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        Plugin plugin = getServer().getPluginManager().getPlugin("SimpleAPI");
        if (plugin == null) {
            Errors.NO_API.print();
            new MissingAPI(this).runTaskTimer(this, 0, 20 * 60 * 2);
            return;
        }
        if (!errorCheck()) {
            setEnabled(false);
            return;
        }

        loadConfig();
        PetType.initiate();
        createPluginInstances();
        saveResource("SimplePets-Info-App.txt", true);
        new VersionNMS().registerPets();
        registerEvents();
        int v = ServerVersion.getVersion().getIntVersion();
        if ((v < 18) || (ServerVersion.getVersion() == ServerVersion.UNKNOWN)) {
            debug("This version is not supported, be sure you are between 1.8.8 and 1.12");
            setEnabled(false);
            return;
        }
        itemLoaders.initiate();
        invLoaders.initiate();

        if (getConfiguration().isSet("MySQL.Enabled")) handleSQL();
        debug("Took " + (System.currentTimeMillis() - start) + "ms to load");
    }

    private void registerEvents() {
        debug("Registering Listeners...");
        getCommand("pet").setExecutor(cmd_pet);
        getServer().getPluginManager().registerEvents(new MainListeners(), this);
        getServer().getPluginManager().registerEvents(new OnJoin(), this);
        getServer().getPluginManager().registerEvents(new ItemStorageMenu(), this);
        getServer().getPluginManager().registerEvents(new PetEventListeners(), this);
        getServer().getPluginManager().registerEvents(new OnPetSpawn(), this);
        getServer().getPluginManager().registerEvents(new SelectionListener(), this);
        getServer().getPluginManager().registerEvents(new DataListener(), this);
    }

    private void createPluginInstances() {
        debug("Creating plugin instances...");
        utilities = new Utilities();
        itemLoaders = new ItemLoaders();
        invLoaders = new InvLoaders();
        cmd_pet = new CMD_Pet();
        linkRetriever = new LinkRetriever();
    }

    private boolean errorCheck() {
        double ver = Double.parseDouble(getServer().getPluginManager().getPlugin("SimpleAPI").getDescription().getVersion());
        if (ver < 3.7) {
            Errors.API_OUT_OF_DATE.print();
            return false;
        }

        SpigotPluginHandler spigotPluginHandler = new SpigotPluginHandler(this, 14124, SpigotPluginHandler.MetricType.BSTATS);
        SpigotPluginHandler.registerPlugin(spigotPluginHandler);

        if (!spigotPluginHandler.runTamperCheck("brainsynder", "SimplePets", "4.0")) {
            return false;
        }
        try {
            Class.forName("org.spigotmc.event.entity.EntityMountEvent");
        } catch (Exception e) {
            Errors.NO_SPIGOT.print();
            return false;
        }

        double version = getJavaVersion();
        if (version == 0.0) {
            Errors.JAVA_WARNING_WEAK.print();
        } else {
            if (!(version >= 1.8)) {
                Errors.JAVA_WARNING_CRITICAL.print();
                return false;
            }
        }


        if (!supportedVersions.contains(Reflection.getVersion())) {
            Errors.UNSUPPORTED_VERSION_CRITICAL.print();
            return false;
        }
        if (!Reflection.getVersion().equals("v1_12_R1")) {
            Errors.UNSUPPORTED_VERSION_WEAK.print();
        }
        return true;
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
                if (!mySQL.hasColumn("UUID")) mySQL.addColumn("UUID", "TEXT");
                if (!mySQL.hasColumn("name")) mySQL.addColumn("name", "TEXT");
                if (!mySQL.hasColumn("UnlockedPets")) mySQL.addColumn("UnlockedPets", "MEDIUMTEXT");
                if (!mySQL.hasColumn("PetName")) mySQL.addColumn("PetName", "TEXT");
                if (!mySQL.hasColumn("NeedsRespawn")) mySQL.addColumn("NeedsRespawn", "MEDIUMTEXT");
            });
            thread.setName("SimplePets SQL");
            thread.setDaemon(false);
            thread.start();
        }
    }

    private void reloadSpawner() {
        ServerVersion version = ServerVersion.getVersion();
        if (version == ServerVersion.v1_11_R1) {
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

    private void loadConfig() {
        debug("Loading Config.yml...");
        configuration = new Config(this, "Config.yml");
        configuration.loadDefaults();
        debug("Loading Messages.yml...");
        messages = new Messages(this, "Messages.yml");
        messages.loadDefaults();
        debug("Loading PetTranslator.yml... (Longest Task)");
        translator = new PetTranslator(this);
        translator.loadDefaults();
    }

    public void onDisable() {
        disabling = true;
        for (PetOwner petOwner : PetOwner.values()) {
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
        ChatColor color = ChatColor.WHITE;
        switch (level) {
            case 1:
                color = ChatColor.YELLOW;
                break;
            case 2:
                color = ChatColor.RED;
                break;
        }
        if (configuration == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[SimplePets Debug] " + color + message);
            return;
        }
        if (!configuration.isSet("Debug.Enabled")) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[SimplePets Debug] " + color + message);
            return;
        }
        if (!configuration.getBoolean("Debug.Enabled")) return;
        if (!configuration.getStringList("Debug.Levels").contains(String.valueOf(level))) return;

        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[SimplePets Debug] " + color + message);
    }

    public void reload () {
        if (getConfiguration().isSet("MySQL.Enabled")) {
            if (!getConfiguration().getBoolean("MySQL.Enabled")) return;
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

    // GETTERS

    public boolean isDisabling() {return this.disabling;}

    public Config getConfiguration() {return this.configuration;}

    public Messages getMessages() {return this.messages;}

    public PetTranslator getTranslator() {
        return translator;
    }

    public MySQL getMySQL() {return this.mySQL;}

    public String getDefaultPetName(PetType petType, Player player) {
        return translateName(petType.getDefaultName()).replace("%player%", player.getName());
    }

    public String translateName(String name) {
        boolean color = getConfiguration().getBoolean("ColorCodes");
        boolean k = getConfiguration().getBoolean("Use&k");
        if (color)
            name = ChatColor.translateAlternateColorCodes('&', k ? name : name.replace("&k", "k"));

        return name;
    }

    public PlayerStorage getPlayerStorage(Player player) {
        if (fileStorage.containsKey(player.getUniqueId()))
            return fileStorage.get(player.getUniqueId());
        PlayerStorage file = new PlayerStorage(player);
        fileStorage.put(player.getUniqueId(), file);
        return fileStorage.get(player.getUniqueId());
    }

    public PlayerStorage getPlayerStorageByName(String name) {
        File folder = new File(getDataFolder().toString() + "/PlayerData/");
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null && files.length != 0) {
                CompletableFuture<PlayerStorage> future = CompletableFuture.supplyAsync(() -> {
                    for (File file : files) {
                        if (file.getName().endsWith(".stc")) {
                            PlayerStorage storage = new PlayerStorage(file);
                            if (!storage.hasKey("username")) return null;

                            if (storage.getString("username").equalsIgnoreCase(name)) {
                                return new PlayerStorage(file);
                            }
                        }
                    }
                    return null;
                });

                try {
                    return future.get();
                } catch (Exception ignored) {}
            }
        }
        return null;
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

    public Utilities getUtilities() {
        return utilities;
    }

    public ItemLoaders getItemLoaders() {
        return itemLoaders;
    }

    public InvLoaders getInvLoaders() {
        return invLoaders;
    }

    public CMD_Pet getCmd_pet() {
        return cmd_pet;
    }

    public LinkRetriever getLinkRetriever() {
        return linkRetriever;
    }

    // SETTERS

    private void setMySQL(MySQL mySQL) {this.mySQL = mySQL; }


    // STATIC

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
}
