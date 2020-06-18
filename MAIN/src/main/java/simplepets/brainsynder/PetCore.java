package simplepets.brainsynder;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.commands.CommandRegistry;
import lib.brainsynder.metric.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.database.MySQL;
import simplepets.brainsynder.links.LinkRetriever;
import simplepets.brainsynder.links.worldedit.WorldEditLink;
import simplepets.brainsynder.listeners.MainListeners;
import simplepets.brainsynder.listeners.OnJoin;
import simplepets.brainsynder.listeners.OnPetSpawn;
import simplepets.brainsynder.listeners.PetEventListeners;
import simplepets.brainsynder.menu.ItemStorageMenu;
import simplepets.brainsynder.menu.inventory.InvLoaders;
import simplepets.brainsynder.menu.inventory.listeners.ArmorListener;
import simplepets.brainsynder.menu.inventory.listeners.DataListener;
import simplepets.brainsynder.menu.inventory.listeners.SavesListener;
import simplepets.brainsynder.menu.inventory.listeners.SelectionListener;
import simplepets.brainsynder.menu.items.ItemLoaders;
import simplepets.brainsynder.nms.VersionNMS;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.pet.TypeManager;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.files.*;
import simplepets.brainsynder.utils.Errors;
import simplepets.brainsynder.utils.ISpawner;
import simplepets.brainsynder.utils.Utilities;

import java.io.File;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PetCore extends JavaPlugin {

    private static PetCore instance;
    private final List<String> supportedVersions = new ArrayList<>();
    public boolean forceSpawn;
    private boolean disabling = false, reloaded = false, needsPermissions = true, needsDataPermissions = true;

    private ItemLoaders itemLoaders;
    private InvLoaders invLoaders;
    private Config configuration;
    private Messages messages;
    private Utilities utilities = null;
    private MySQL mySQL = null;
    private LinkRetriever linkRetriever;
    private TypeManager typeManager;
    private Commands commands;
    private EconomyFile ecomony;

    private ISpawner spawner;
    private final Map<UUID, PlayerStorage> fileStorage = new HashMap<>();

    public void onEnable() {
        Utilities.findDelay(getClass(), "startup", false);
        instance = this;
        if (ServerVersion.isOlder(ServerVersion.v1_11_R1)) {
            debug("This version is not supported, be sure you are between 1.11 and 1.14");
            setEnabled(false);
            return;
        }
        if (!errorCheck()) {
            setEnabled(false);
            return;
        }
        // Oh no... Someone is reloading the server/plugin
        // ALERT THE OPS !!!
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            reloaded = true;
            Errors.RELOAD_DETECTED.print();
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player.isOp()) {
                    player.sendMessage("§6[§eSimplePets§6] §7SimplePets has detected a reload, If §c§lANY§7 issues arise then please restart the server.");
                }
            });
        }

        typeManager = new TypeManager(this);
        loadConfig();
        createPluginInstances();
        new VersionNMS().registerPets();
        registerEvents();
        itemLoaders.initiate();
        invLoaders.initiate();

        // Lets run this later, just to make sure WorldEdit is loaded
        new BukkitRunnable() {
            @Override
            public void run() {
                linkRetriever.initiate();
                WorldEditLink.init();
            }
        }.runTaskLater(this, 20 * 10);
        reloadSpawner();
        spawner.init();
        if (getConfiguration().isSet("MySQL.Enabled")) handleSQL();
        debug("Took " + Utilities.findDelay(getClass(), "startup", false) + "ms to load");
    }

    private void registerEvents() {
        debug("Registering Listeners...");
        try {
            new CommandRegistry(this).register(new PetCommand());
        } catch (Exception e) {
            e.printStackTrace();
        }
        getServer().getPluginManager().registerEvents(new MainListeners(), this);
        getServer().getPluginManager().registerEvents(new OnJoin(), this);
        getServer().getPluginManager().registerEvents(new ItemStorageMenu(), this);
        getServer().getPluginManager().registerEvents(new PetEventListeners(), this);
        getServer().getPluginManager().registerEvents(new OnPetSpawn(), this);
        getServer().getPluginManager().registerEvents(new SelectionListener(), this);
        getServer().getPluginManager().registerEvents(new DataListener(), this);
        getServer().getPluginManager().registerEvents(new SavesListener(), this);
        getServer().getPluginManager().registerEvents(new ArmorListener(), this);
    }

    private void createPluginInstances() {
        debug("Creating plugin instances...");
        utilities = new Utilities();
        itemLoaders = new ItemLoaders();
        invLoaders = new InvLoaders();
        linkRetriever = new LinkRetriever();
    }

    private boolean errorCheck() {
        new Metrics(this);

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
            debug("Using Java: " + version);
        }

        fetchSupportedVersions();
        if (supportedVersions.isEmpty()) {
            Errors.UNSUPPORTED_VERSION_CRITICAL.print();
            return false;
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

            debug("Creating SQL table if there is none...");
            CompletableFuture.runAsync(() -> {
                try (Connection connection = mySQL.getSource().getConnection()) {
                    connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `SimplePets` (`UUID` TEXT,`name` TEXT,`UnlockedPets` MEDIUMTEXT,`PetName` TEXT,`NeedsRespawn` MEDIUMTEXT);");
                    if (!mySQL.hasColumn(connection, "SavedPets"))
                        mySQL.addColumn(connection, "SavedPets", "LONGTEXT");
                } catch (Exception e) {
                    debug("Unable to create default SQL tables Error:");
                    e.printStackTrace();
                }
            });
        }
    }

    private void reloadSpawner() {
        ServerVersion version = ServerVersion.getVersion();
        try {
            Class<?> clazz = Class.forName("simplepets.brainsynder.nms." + version.name() + ".entities.SpawnUtil");
            if (clazz == null) return;
            if (ISpawner.class.isAssignableFrom(clazz)) {
                spawner = (ISpawner) clazz.getConstructor().newInstance();
                debug("Successfully Linked to " + version.name() + " SpawnUtil Class");
            }
        } catch (Exception e) {
            debug("Could not link to a SpawnUtil Class... Possible Wrong version?");
        }
    }

    private void loadConfig() {
        debug("Loading Config.yml...");
        configuration = new Config(this, "Config.yml");
        configuration.loadDefaults();
        needsPermissions = configuration.getBoolean("Needs-Permission");
        needsDataPermissions = configuration.getBoolean("Needs-Data-Permissions");
        debug("Loading Messages.yml...");
        messages = new Messages(this, "Messages.yml");
        messages.loadDefaults();
        debug("Loading Commands.yml...");
        commands = new Commands(this, "Commands.yml");
        commands.loadDefaults();
        debug("Loading PetEconomy.yml...");
        ecomony = new EconomyFile();
        ecomony.loadDefaults();
    }

    public void onDisable() {
        if (typeManager != null) {
            typeManager.unLoad();
        }
        if (linkRetriever != null) linkRetriever.cleanup();
        disabling = true;
        for (PetOwner petOwner : PetOwner.values()) {
            if (petOwner == null) continue;
            if (petOwner.getPlayer() == null) continue;
            if (!petOwner.getPlayer().isOnline()) continue;
            if (petOwner.hasPet()) petOwner.removePet();
            petOwner.getFile().save(true, true);
        }
        if (getConfiguration() != null) {
            if (getConfiguration().getBoolean("MySQL.Enabled")) {
                if (mySQL != null) mySQL = null;
            }
        }
        try {
            Thread.sleep(20L);
        } catch (InterruptedException ignored) {
        }
    }

    public TypeManager getTypeManager() {
        return typeManager;
    }

    public boolean wasReloaded() {
        return reloaded;
    }

    public void debug(String message) {
        debug(0, message);
    }

    public void debug(int level, String message) {
        if (level >= 3) level = 2;
        ChatColor prefix = ((level == -1) ? ChatColor.AQUA : ChatColor.GOLD);
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
            Bukkit.getConsoleSender().sendMessage(prefix + "[SimplePets Debug] " + color + message);
            return;
        }
        if (!configuration.isSet("Debug.Enabled")) {
            Bukkit.getConsoleSender().sendMessage(prefix + "[SimplePets Debug] " + color + message);
            return;
        }
        if (level != -1) {
            if (!configuration.getBoolean("Debug.Enabled")) return;
            if (!configuration.getStringList("Debug.Levels").contains(String.valueOf(level))) return;
        }
        Bukkit.getConsoleSender().sendMessage(prefix + "[SimplePets Debug] " + color + message);
    }

    public void reload(int type) {
        needsPermissions = configuration.getBoolean("Needs-Permission");
        needsDataPermissions = configuration.getBoolean("Needs-Data-Permissions");
        if ((type == 0) || (type == 2)) {
            if (typeManager != null) {
                typeManager.unLoad();
                typeManager = null;
            }
            typeManager = new TypeManager(this);
        }

        if ((type == 1) || (type == 2)) {
            if (getConfiguration().isSet("MySQL.Enabled")) {
                if (!getConfiguration().getBoolean("MySQL.Enabled")) return;
                String host = getConfiguration().getString("MySQL.Host", false);
                String port = getConfiguration().getString("MySQL.Port", false);
                String databaseName = getConfiguration().getString("MySQL.DatabaseName", false);
                String username = getConfiguration().getString("MySQL.Login.Username", false);
                String password = getConfiguration().getString("MySQL.Login.Password", false);
                mySQL = new MySQL(host, port, databaseName, username, password);
                CompletableFuture.runAsync(() -> {
                    try (Connection connection = mySQL.getSource().getConnection()) {
                        connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `SimplePets` (`UUID` TEXT,`name` TEXT,`UnlockedPets` MEDIUMTEXT,`PetName` TEXT,`NeedsRespawn` MEDIUMTEXT);");
                        if (!mySQL.hasColumn(connection, "SavedPets"))
                            mySQL.addColumn(connection, "SavedPets", "LONGTEXT");
                    } catch (Exception e) {
                        debug("Unable to create default SQL tables Error:");
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    // GETTERS

    private void fetchSupportedVersions() {
        supportedVersions.clear();
        String current = ServerVersion.getVersion().name();
        boolean supported = false;
        String packageName = "simplepets.brainsynder.nms.<VER>.anvil.HandleAnvilGUI";
        for (ServerVersion version : ServerVersion.values()) {
            if (version.name().equals(current) && (!supported)) supported = true;
            try {
                Class<?> clazz = Class.forName(packageName.replace("<VER>", version.name()), false, getClassLoader());
                if (clazz != null) supportedVersions.add(version.name());
            } catch (Exception e) {
            }
        }
        if (!supported) {
            try {
                Class<?> clazz = Class.forName(packageName.replace("<VER>", current), false, getClassLoader());
                if (clazz != null) supportedVersions.add(current);
            } catch (Exception e) {
            }
        }

        if (!supportedVersions.isEmpty())
            debug("Found support for version(s): " + supportedVersions.toString());
    }

    public boolean needsPermissions() {
        return needsPermissions;
    }

    public boolean needsDataPermissions() {
        return needsDataPermissions;
    }

    public boolean isDisabling() {
        return this.disabling;
    }

    public Config getConfiguration() {
        return this.configuration;
    }

    public Messages getMessages() {
        return this.messages;
    }

    public MySQL getMySQL() {
        return this.mySQL;
    }

    public Commands getCommands() {
        return commands;
    }

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

    public void getPlayerStorage(Player player, boolean async, Call<PlayerStorage> callback) {
        // Ensure it's called async
        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                handlePlStorage(player, callback);
            });
        } else {
            handlePlStorage(player, callback);
        }
    }

    private void handlePlStorage(Player player, Call<PlayerStorage> callback) {
        PlayerStorage storage;
        if (!fileStorage.containsKey(player.getUniqueId())) {
            PlayerStorage file = new PlayerStorage(player);
            fileStorage.put(player.getUniqueId(), file);
        }
        storage = fileStorage.get(player.getUniqueId());
        if (storage == null) {
            callback.onFail();
        } else {
            callback.call(storage);
        }
    }

    public void getPlayerStorageByName(String name, Call<PlayerStorage> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            PlayerStorage storage = null;
            File folder = new File(getDataFolder().toString() + "/PlayerData/");
            if (folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files != null && files.length != 0) {
                    for (File file : files) {
                        if (file.getName().endsWith(".stc")) {
                            storage = new PlayerStorage(file);
                            if (!storage.hasKey("username")) {
                                callback.onFail();
                                return;
                            }
                            if (!storage.getString("username").equalsIgnoreCase(name)) {
                                callback.onFail();
                                return;
                            }
                        }
                    }
                }
            }
            if (storage == null) {
                callback.onFail();
            } else {
                callback.call(storage);
            }
        });
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
            if (version.contains(".")) {
                int pos = version.indexOf('.');
                pos = version.indexOf('.', pos + 1);
                version = version.substring(0, pos);
            }
            return Double.parseDouble(version);
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

    public LinkRetriever getLinkRetriever() {
        return linkRetriever;
    }

    public EconomyFile getEcomony() {
        return ecomony;
    }

    // STATIC
    public static PetCore get() {
        return instance;
    }

    public static boolean hasPerm(Player p, String perm) {
        if (get().configuration.getBoolean("Needs-Permission")) {
            return p.hasPermission(perm);
        }
        return true;
    }

    public ClassLoader getLoader() {
        return getClassLoader();
    }

    public interface Call<T> {
        void call(T data);

        default void onFail() {}
    }
}
