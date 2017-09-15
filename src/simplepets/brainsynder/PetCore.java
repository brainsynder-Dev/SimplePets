package simplepets.brainsynder;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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
import simplepets.brainsynder.errors.SimplePetsException;
import simplepets.brainsynder.events.*;
import simplepets.brainsynder.files.*;
import simplepets.brainsynder.links.IProtectionLink;
import simplepets.brainsynder.links.impl.WorldGuardLink;
import simplepets.brainsynder.menu.ItemStorageMenu;
import simplepets.brainsynder.menu.MenuItem;
import simplepets.brainsynder.nms.VersionNMS;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.nms.entities.v1_8_R3.SpawnUtil;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.ReflectionUtil;
import simplepets.brainsynder.utils.ISpawner;
import simplepets.brainsynder.utils.LoaderRetriever;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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
    private DataSource datasource;
    @Getter
    private boolean disabling = false;
    @Getter
    private Config configuration;
    @Getter
    private Messages messages;
    @Getter
    private IProtectionLink worldGuardLink;
    private ISpawner spawner;
    @Getter
    @Setter
    private IStorage<Integer> availableSlots = new StorageList<>();
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

    public DataSource getDataSource() {
        if (datasource == null) {
            if (configuration.isSet("MySQL.Enabled")) {
                if (configuration.getBoolean("MySQL.Enabled")) {
                    debug("Loading MySQL support... (This is in beta still)");
                    String host = configuration.getString("MySQL.Host", false);
                    String port = configuration.getString("MySQL.Port", false);
                    String databaseName = configuration.getString("MySQL.DatabaseName", false);
                    String username = configuration.getString("MySQL.Login.Username", false);
                    String password = configuration.getString("MySQL.Login.Password", false);

                    HikariConfig config = new HikariConfig();
                    config.setJdbcUrl(host + ':' + port + '/' + databaseName);
                    config.setUsername(username);
                    config.setPassword(password);
                    config.setMaximumPoolSize(10);
                    config.setAutoCommit(false);
                    config.addDataSourceProperty("cachePrepStmts", "true");
                    config.addDataSourceProperty("prepStmtCacheSize", "250");
                    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                    datasource = new HikariDataSource(config);
                    createTable();
                }
            }
        }
        return datasource;
    }

    public void onEnable() {
        Plugin plugin = getServer().getPluginManager().getPlugin("SimpleAPI");
        if (plugin == null) {
            System.out.println("SimplePets >> Missing dependency (SimpleAPI) Must have the plugin in order to work the plugin");
            new MissingAPI(this).runTaskTimer(this, 0, 20 * 60 * 2);
            return;
        }
        double ver = Double.parseDouble(plugin.getDescription().getVersion());
        if (ver < 3.6) {
            System.out.println("SimplePets >> Notice: Your Version of SimpleAPI is OutOfDate, Please update SimpleAPI https://www.spigotmc.org/resources/24671/");
            System.out.println("Disabling SimplePets...");
            setEnabled(false);
            return;
        }
        SpigotPluginHandler spigotPluginHandler = new SpigotPluginHandler(this, 14124, SpigotPluginHandler.MetricType.BSTATS);
        SpigotPluginHandler.registerPlugin(spigotPluginHandler);
        if (!spigotPluginHandler.runTamperCheck("brainsynder", "SimplePets", "3.7")) {
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
        if (!(getJavaVersion() >= 1.8)) {
            System.out.println("-------------------------------------------");
            System.out.println("          Error Type: CRITICAL");
            System.out.println("    An Internal Version Error Occurred");
            System.out.println("SimplePets Requires Java 8+ in order to work. Please update Java.");
            System.out.println("-------------------------------------------");
            setEnabled(false);
            return;
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
        getServer().getPluginManager().registerEvents(new OnHurtPet(), this);
        getServer().getPluginManager().registerEvents(new OnJoin(), this);
        getServer().getPluginManager().registerEvents(new ItemStorageMenu(), this);
        getServer().getPluginManager().registerEvents(new PetEventListeners(this), this);
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

        //String permFile = "DataPermissions.yml";
        List<PetType> types = new ArrayList<>();
        //StringBuilder builder = new StringBuilder();
        for (PetType type : PetType.values()) {
            /*builder.append(type.getConfigName()).append(':').append('\n');
            builder.append("  - Flight Permission (If Fly is set to true in the PetTranslator.yml):").append('\n');
            builder.append("     - ").append(type.getPermission()).append(".fly").append('\n');
            builder.append("  - Hat Permission (If Hat is set to true in the PetTranslator.yml):").append('\n');
            builder.append("     - ").append(type.getPermission()).append(".hat").append('\n');
            builder.append("  - Mount Permission (If Mount is set to true in the PetTranslator.yml):").append('\n');
            builder.append("     - ").append(type.getPermission()).append(".mount").append('\n');

            if (type.getPetData() != null) {
                builder.append("  - Data Permissions (For When you R-Click your Pet):").append('\n');
                builder.append("    - ").append(type.getPermission()).append(".*").append('\n');
                for (Class<? extends MenuItem> clazz : type.getPetData().getItemClasses()) {
                    MenuItem item = getItem(type, clazz);
                    builder.append("    - ").append(item.getPermission()).append('\n');
                }
            }*/

            if (type.isSupported()) {
                if (type.isEnabled()) {
                    types.add(type);
                }
            }
        }
        //printOut(false, permFile, builder.toString());
        petTypes = new ObjectPager<>(size, types);
        worldGuardLink = new WorldGuardLink();
        if (configuration.isSet("MySQL.Enabled")) {
            if (configuration.getBoolean("MySQL.Enabled")) {
                debug("Loading MySQL support... (This is in beta still)");
                createTable();
            }
        }
    }

    private MenuItem getItem(PetType type, Class<? extends MenuItem> clazz) {
        try {
            return ReflectionUtil.initiateClass(ReflectionUtil.fillConstructor(clazz, PetType.class, IEntityPet.class), type, null);
        } catch (Exception e) {
            return null;
        }
    }

    public void printOut(boolean date, String filename, String message) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        StringBuilder builder = new StringBuilder();
        if (date) {
            builder.append('[').append(format.format(calendar.getTime())).append("] ");
        }
        builder.append(message);
        try {
            File saveTo = new File(getDataFolder(), filename);
            FileWriter fw = new FileWriter(saveTo, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(builder.toString());
            pw.flush();
            pw.close();
        } catch (IOException var7) {
            System.out.println("Unable to save '" + filename + "' contents");
        }
    }

    public ISpawner getSpawner() {
        if (spawner == null) {
            reloadSpawner();
        }
        return spawner;
    }

    private double getJavaVersion() {
        String version = System.getProperty("java.version");
        int pos = version.indexOf('.');
        pos = version.indexOf('.', pos + 1);
        return Double.parseDouble(version.substring(0, pos));
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

    private void createTable() {
        Connection connection = null;
        try {
            connection = getDataSource().getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `SimplePets`(`UUID` varchar(36), `name` varchar(16), `UnlockedPets` text, `PetName` varchar(150), `NeedsRespawn` varchar(255))");
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new SimplePetsException("Could not Rollback the Connection Cause:" + e1.getMessage(), e1);
            }
            throw new SimplePetsException("Could not check the SimplePets Table Cause:" + e.getMessage(), e);
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
            if (folder.listFiles().length != 0) {
                for (File file : folder.listFiles()) {
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
        return null;
    }

    private void loadConfig() {
        configuration = new Config(this, "Config.yml");
        configuration.loadDefaults();
        debug("Loading Config values");
        messages = new Messages(this, "Messages.yml");
        messages.loadDefaults();
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
}
