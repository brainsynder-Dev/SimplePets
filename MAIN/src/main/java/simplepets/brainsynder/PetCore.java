package simplepets.brainsynder;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.commands.CommandRegistry;
import lib.brainsynder.json.WriterConfig;
import lib.brainsynder.metric.bukkit.Metrics;
import lib.brainsynder.update.UpdateResult;
import lib.brainsynder.update.UpdateUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
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
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.pet.TypeManager;
import simplepets.brainsynder.player.MySQLHandler;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.files.*;
import simplepets.brainsynder.utils.*;

import java.io.File;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class PetCore extends JavaPlugin {

    private static PetCore instance;
    private final List<String> supportedVersions = new ArrayList<>();
    public boolean forceSpawn;
    private boolean disabling = false;
    private boolean reloaded = false;
    private boolean needsPermissions = true;
    private boolean needsDataPermissions = true;
    private final boolean needsUpdate = false;

    private ItemLoaders itemLoaders;
    private InvLoaders invLoaders;
    private Config configuration;
    private Messages messages;
    private Utilities utilities = null;
    private MySQL mySQL = null;
    private MySQLHandler sqlHandler = null;
    private LinkRetriever linkRetriever;
    private TypeManager typeManager;
    private Commands commands;
    private EconomyFile ecomony;

    private ISpawner spawner;
    private final Map<UUID, PlayerStorage> fileStorage = new HashMap<>();

    private UpdateUtils updateUtils;
    private UpdateResult updateResult;

    @Override
    public void onLoad() {
        instance = this;
        linkRetriever = new LinkRetriever();
        linkRetriever.earlyInitiate();
    }

    public void onEnable() {
        TimerUtil.findDelay(getClass(), "startup");

        fetchSupportedVersions();

        if (ServerVersion.isOlder(ServerVersion.v1_15_R1)) {
            debug(DebugLevel.DEBUG, "This version is not supported, be sure you are " + supportedVersions.toString());
            setEnabled(false);
            return;
        }
        if (!errorCheck()) {
            TimerUtil.findDelay(getClass(), "ErrorCheck");
            setEnabled(false);
            return;
        }
        TimerUtil.findDelay(getClass(), "ErrorCheck");
        // Oh no... Someone is reloading the server/plugin
        // ALERT THE OPS !!!
        if (!Bukkit.getOnlinePlayers().isEmpty() || disabling) {
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
        registerEvents();
        itemLoaders.initiate();
        invLoaders.initiate();

        // Lets run this later, just to make sure WorldEdit is loaded
        new BukkitRunnable() {
            @Override
            public void run() {
                //     linkRetriever.initiate();
                WorldEditLink.init();
            }
        }.runTaskLater(this, 20 * 10);
        reloadSpawner();
        spawner.init();
        linkRetriever.initiate();
        if (getConfiguration().isSet("MySQL.Enabled")) handleSQL();
        debug("Took " + TimerUtil.findDelay(getClass(), "startup") + "ms to load");

        // Handle Update checking
        updateResult = new UpdateResult().setPreStart(() -> debug(DebugLevel.UPDATE, "Checking for new builds..."))
                .setFailParse(members -> debug(DebugLevel.UPDATE, "Data collected: " + members.toString(WriterConfig.PRETTY_PRINT)))
                .setNoNewBuilds(() -> debug(DebugLevel.UPDATE, "No new builds were found"))
                .setOnError(() -> debug(DebugLevel.UPDATE, "An error occurred when checking for an update"))
                .setNewBuild(members -> {
                    int latestBuild = members.getInt("build", -1);

                    // New build found
                    if (latestBuild > updateResult.getCurrentBuild()) {
                        debug(DebugLevel.UPDATE, "You are " + (latestBuild - updateResult.getCurrentBuild()) + " build(s) behind the latest.");
                        debug(DebugLevel.UPDATE, "https://ci.pluginwiki.us/job/" + updateResult.getRepo() + "/" + latestBuild + "/");
                    }
                });
        updateUtils = new UpdateUtils(this, updateResult);

        if (!configuration.getBoolean("Update-Checking.Enabled", true)) return;
        int time = configuration.getInt("Update-Checking.time", 12);
        TimeUnit unit;

        String timeunit = configuration.getString("Update-Checking.unit", "HOURS");
        try {
            unit = TimeUnit.valueOf(timeunit);
        }catch (Exception e) {
            unit = TimeUnit.HOURS;
            debug(DebugLevel.ERROR, "Could not find unit for '"+timeunit+"'");
        }

        updateUtils.startUpdateTask(time, unit); // Runs the update check every 12 hours
    }

    private void registerEvents() {
        debug("Registering Listeners...");
        TimerUtil.findDelay(getClass(), "Registering Command");
        try {
            new CommandRegistry(this).register(new PetCommand());
        } catch (Exception e) {
            e.printStackTrace();
        }
        TimerUtil.findDelay(getClass(), "Registering Command");
        TimerUtil.findDelay(getClass(), "Registering Listeners");
        getServer().getPluginManager().registerEvents(new MainListeners(), this);
        getServer().getPluginManager().registerEvents(new OnJoin(), this);
        getServer().getPluginManager().registerEvents(new ItemStorageMenu(), this);
        getServer().getPluginManager().registerEvents(new PetEventListeners(), this);
        getServer().getPluginManager().registerEvents(new OnPetSpawn(), this);
        getServer().getPluginManager().registerEvents(new SelectionListener(), this);
        getServer().getPluginManager().registerEvents(new DataListener(), this);
        getServer().getPluginManager().registerEvents(new SavesListener(), this);
        getServer().getPluginManager().registerEvents(new ArmorListener(), this);
        TimerUtil.findDelay(getClass(), "Registering Listeners");
    }

    private void createPluginInstances() {
        debug("Creating plugin instances...");
        utilities = new Utilities();
        itemLoaders = new ItemLoaders();
        invLoaders = new InvLoaders();
    }

    private boolean errorCheck() {
        TimerUtil.findDelay(getClass(), "ErrorCheck");
        new Metrics(this);

        try {
            Class.forName("org.spigotmc.event.entity.EntityMountEvent");
        } catch (Exception e) {
            Errors.NO_SPIGOT.print();
            return false;
        }

        double version = getJavaVersion();
        debug("Using Java: " + version);
        if (version == 0.0) {
            Errors.JAVA_WARNING_WEAK.print();
        } else {
            if (!(version >= 1.8)) {
                Errors.JAVA_WARNING_CRITICAL.print();
                return false;
            }
        }

        if (supportedVersions.isEmpty()) {
            Errors.UNSUPPORTED_VERSION_CRITICAL.print();
            return false;
        }
        return true;
    }

    private void handleSQL() {
        TimerUtil.findDelay(getClass(), "MySQL");
        if (getConfiguration().getBoolean("MySQL.Enabled", false)) {
            String host = getConfiguration().getString("MySQL.Host", false);
            String port = getConfiguration().getString("MySQL.Port", false);
            String databaseName = getConfiguration().getString("MySQL.DatabaseName", false);
            String username = getConfiguration().getString("MySQL.Login.Username", false);
            String password = getConfiguration().getString("MySQL.Login.Password", false);
            mySQL = new MySQL(host, port, databaseName, username, password);

            debug("Creating SQL table if there is none...");
            CompletableFuture.runAsync(() -> {
                try (Connection connection = mySQL.getSource().getConnection()) {
                    connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `SimplePets` (`UUID` VARCHAR(128),`name` TEXT, PRIMARY KEY (`UUID`));");
                    StringBuilder builder = new StringBuilder();
                    if (!mySQL.hasColumn(connection, "UUID")) {
                        mySQL.addColumn(connection, "UUID", "TEXT");
                        builder.append(", UUID");
                    }
                    if (!mySQL.hasColumn(connection, "name")) {
                        mySQL.addColumn(connection, "name", "TEXT");
                        builder.append(", name");
                    }
                    if (!mySQL.hasColumn(connection, "UnlockedPets")) {
                        mySQL.addColumn(connection, "UnlockedPets", "MEDIUMTEXT");
                        builder.append(", UnlockedPets");
                    }
                    if (!mySQL.hasColumn(connection, "PetName")) {
                        mySQL.addColumn(connection, "PetName", "TEXT");
                        builder.append(", PetName");
                    }
                    if (!mySQL.hasColumn(connection, "NeedsRespawn")) {
                        mySQL.addColumn(connection, "NeedsRespawn", "MEDIUMTEXT");
                        builder.append(", NeedsRespawn");
                    }
                    if (!mySQL.hasColumn(connection, "SavedPets")) {
                        mySQL.addColumn(connection, "SavedPets", "LONGTEXT");
                        builder.append(", SavedPets");
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            sqlHandler = new MySQLHandler(instance, mySQL);
                            if (!builder.toString().isEmpty())
                                debug("Database is missing column(s) adding: " + builder.toString().replaceFirst(", ", ""));
                        }
                    }.runTask(instance);
                } catch (Exception e) {
                    debug("Unable to create default SQL tables Error:");
                    e.printStackTrace();
                }
            });
        }
        TimerUtil.findDelay(getClass(), "MySQL");
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
        TimerUtil.findDelay(getClass(), "Loading Config Files");
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
        TimerUtil.findDelay(getClass(), "Loading Config Files");
    }

    public void onDisable() {
        if (typeManager != null) typeManager.unLoad();
        if (linkRetriever != null) linkRetriever.cleanup();
        disabling = true;
        if (updateUtils != null) updateUtils.stopTask();

        TimerUtil.findDelay(getClass(), "Saving PlayerData");
        for (PetOwner petOwner : PetOwner.values()) {
            if (petOwner == null) continue;
            if (petOwner.getPlayer() == null) continue;
            if (!petOwner.getPlayer().isOnline()) continue;
            if (petOwner.hasPet()) petOwner.removePet();
            petOwner.getFile().save(true, true);
        }
        TimerUtil.findDelay(getClass(), "Saving PlayerData");
        TimerUtil.outputTimings();

        try {
            Thread.sleep(20L);
        } catch (InterruptedException ignored) {
        }

        if (getConfiguration() != null) {
            if (getConfiguration().getBoolean("MySQL.Enabled", false)) {
                if (mySQL != null) mySQL = null;
            }
        }
    }

    public TypeManager getTypeManager() {
        return typeManager;
    }

    public boolean wasReloaded() {
        return reloaded;
    }

    public void debug(String message) {
        debug(message, true);
    }

    public void debug(String message, boolean sync) {
        debug(DebugLevel.NORMAL, message, sync);
    }

    public void debug(DebugLevel level, String message) {
        debug(level, message, true);
    }

    public void debug(DebugLevel level, String message, boolean sync) {
        if (!isEnabled()) return;
        Runnable runnable = () -> {
            if ((level != DebugLevel.DEBUG) && (level != DebugLevel.UPDATE)) {
                if (configuration == null) return;
                if (!configuration.getBoolean("Debug.Enabled", false)) return;
                if (!configuration.getStringList("Debug.Levels").contains(String.valueOf(level.getLevel()))) return;
            }
            Bukkit.getConsoleSender().sendMessage(level.getPrefix() + "[SimplePets " + level.getString() + "] " + level.getColor() + message);
        };

        if (sync) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            }.runTask(this);
        } else {
            runnable.run();
        }
    }

    public void reload(int type) {
        configuration.reload();
        commands.reload();
        messages.reload();
        ecomony.reload();

        needsPermissions = configuration.getBoolean("Needs-Permission");
        needsDataPermissions = configuration.getBoolean("Needs-Data-Permissions");
        if ((type == 0) || (type == 2)) {
            if (typeManager != null) {
                typeManager.unLoad();
                typeManager = null;
            }
            typeManager = new TypeManager(this);
        }

        if ((type == 1) || (type == 2)) handleSQL();

    }

    // GETTERS

    private void fetchSupportedVersions() {
        TimerUtil.findDelay(getClass(), "SupportedVersions");
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
        TimerUtil.findDelay(getClass(), "SupportedVersions");
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

    public MySQLHandler getSqlHandler() {
        return sqlHandler;
    }

    public Commands getCommands() {
        return commands;
    }

    public String getDefaultPetName(PetType petType, Player player) {
        return translateName(petType.getDefaultName()).replace("%player%", player.getName());
    }

    public String translateName(String name) {
        boolean color = getConfiguration().getBoolean(Config.COLOR);
        boolean magic = getConfiguration().getBoolean(Config.MAGIC);
        if (color)
            name = ChatColor.translateAlternateColorCodes('&', magic ? name : name.replace("&k", "k"));

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
        return hasPerm(p, perm, false) == 1;
    }

    public static int hasPerm(Player p, String perm, boolean strict) {
        if (get().configuration.getBoolean("Needs-Permission")) {
            if (strict) {
                for (PermissionAttachmentInfo info : p.getEffectivePermissions()) {
                    if (info.getPermission().equalsIgnoreCase(perm)) {
                        return info.getValue() ? 1 : 0;
                    }
                }
                return -1;
            } else {
                return p.hasPermission(perm) ? 1 : 0;
            }
        } else {
            return 1;
        }
    }

    public static Object getHandle(Entity e) {
        return get().getSpawner().getHandle(e);
    }

    public ClassLoader getLoader() {
        return getClassLoader();
    }

    public interface Call<T> {
        void call(T data);

        default void onFail() {}
    }

    public UpdateUtils getUpdateUtils() {
        return updateUtils;
    }
}
