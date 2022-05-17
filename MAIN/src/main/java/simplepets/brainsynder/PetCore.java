package simplepets.brainsynder;

import com.jeff_media.updatechecker.UpdateChecker;
import lib.brainsynder.ServerVersion;
import lib.brainsynder.commands.CommandRegistry;
import lib.brainsynder.json.WriterConfig;
import lib.brainsynder.metric.bukkit.Metrics;
import lib.brainsynder.reflection.Reflection;
import lib.brainsynder.update.UpdateResult;
import lib.brainsynder.update.UpdateUtils;
import lib.brainsynder.utils.TaskTimer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.addon.AddonLocalData;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.inventory.handler.GUIHandler;
import simplepets.brainsynder.api.inventory.handler.ItemHandler;
import simplepets.brainsynder.api.pet.PetConfigManager;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.IPetsPlugin;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.plugin.utils.IPetUtilities;
import simplepets.brainsynder.api.user.UserManagement;
import simplepets.brainsynder.commands.PetsCommand;
import simplepets.brainsynder.commands.list.DebugCommand;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.debug.DebugLogger;
import simplepets.brainsynder.files.Config;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.impl.PetConfiguration;
import simplepets.brainsynder.impl.PetOwner;
import simplepets.brainsynder.impl.PetUtility;
import simplepets.brainsynder.listeners.*;
import simplepets.brainsynder.managers.*;
import simplepets.brainsynder.sql.PlayerSQL;
import simplepets.brainsynder.utils.JavaVersion;
import simplepets.brainsynder.utils.Premium;
import simplepets.brainsynder.utils.debug.Debug;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PetCore extends JavaPlugin implements IPetsPlugin {
    private final List<String> supportedVersions = new ArrayList<>();
    private static PetCore instance;

    private File itemFolder;
    private boolean reloaded = false;
    private boolean fullyStarted = false;
    private boolean isStarting = false;

    private Config configuration;

    private ISpawnUtil SPAWN_UTIL;
    private UserManager USER_MANAGER;
    private PetConfiguration PET_CONFIG;
    private RenameManager renameManager;
    private ItemManager itemManager;
    private InventoryManager inventoryManager;
    private ParticleManager particleManager;
    private AddonManager addonManager;

    private UpdateUtils updateUtils;
    private UpdateResult updateResult;

    private Debug debug;
    private IPetUtilities petUtilities;
    private TaskTimer taskTimer;

    public final Executor sync = task -> Bukkit.getScheduler().runTask(this, task);
    public final Executor async = task -> Bukkit.getScheduler().runTaskAsynchronously(this, task);

    @Override
    public void onEnable() {
        instance = this;
        SimplePets.setPLUGIN(this);
        taskTimer = new TaskTimer(this);
        taskTimer.start();
        isStarting = true;

        debug = new Debug(this);

        if (!checkJavaVersion()){
            setEnabled(false);
            isStarting = false;
            return;
        }

        if (!fetchSupportedVersions()) {
            Bukkit.getPluginManager().registerEvents(new BrokenVersionListener(), this);
            debug.debug(DebugBuilder.build(getClass())
                    .setLevel(DebugLevel.CRITICAL)
                    .setBroadcast(true)
                    .setMessages(
                            "OH NO! We could not find any support for your servers version " + ServerVersion.getVersion().name().replace("v", "").replace("_", "."),
                            "Please check the Jenkins for an updated build: https://ci.pluginwiki.us/job/SimplePets_v5/",
                            "Check if there is a SimplePets-" + ServerVersion.getVersion().name().replace("v", "").replace("_", ".") + ".jar (IF AVAILABLE)"
                    )
            );
            isStarting = false;
            return;
        }
        debug.debug(DebugLevel.HIDDEN, "Setting API instance");
        petUtilities = new PetUtility();

        taskTimer.label("registered api instance");


        itemFolder = new File(getDataFolder() + File.separator + "Items");

        MessageFile.init(getDataFolder());
        taskTimer.label("init messages");

        debug.debug(DebugLevel.HIDDEN, "Initializing Config file");
        configuration = new Config(this);
        configuration.initValues();
        taskTimer.label("init config");

        reloaded = ConfigOption.INSTANCE.RELOAD_DETECT.getValue();
        debug.debug(DebugLevel.HIDDEN, "Plugin reloaded: " + reloaded);

//        debug.debug(DebugLevel.HIDDEN, "Initializing Inventory SQL");
//        new InventorySQL();
//        taskTimer.label("init InventorySQL");
        reloadSpawner();
        taskTimer.label("located spawner util");

        handleManagers();
        taskTimer.label("init managers");

        debug.debug(DebugLevel.HIDDEN, "Initializing Player SQL");
        new PlayerSQL();
        taskTimer.label("init PlayerSQL");

        handleMetrics();
        taskTimer.label("init metrics");

        try {
            debug.debug(DebugLevel.HIDDEN, "Registering commands");
            new CommandRegistry<>(this).register(new PetsCommand(this));
            taskTimer.label("init commands");
        } catch (Exception e) {
            e.printStackTrace();
        }

        handleListeners();
        taskTimer.label("registered listeners");
        handleUpdateUtils();
        taskTimer.label("init update checking");

        addonManager = new AddonManager(this);

        new BukkitRunnable() {
            @Override
            public void run() {
                addonManager.initialize();
                addonManager.checkAddons();
            }
        }.runTaskLater(this, 60);
        taskTimer.label("init addons");
        taskTimer.label("addon update check");

        checkWorldGuard(value -> {
            if (value) {
                debug.debug(DebugBuilder.build(getClass()).setLevel(DebugLevel.CRITICAL)
                        .setMessages(
                                "Your server is using WorldGuard and the 'mobs.block-plugin-spawning' is set to true",
                                "This causes issues with the plugin not being able to spawn pets",
                                "Please set this to 'false' in the WorldGuard config so pets can spawn"
                        ));
            }
        });
        taskTimer.stop();

        fullyStarted = true;

        if (Bukkit.getOnlinePlayers().isEmpty()) return;
        // Delay it for a second to actually have the database load
        new BukkitRunnable() {
            @Override
            public void run() {
                debug.debug(DebugLevel.HIDDEN, "Respawning pets for players (if there are any)");
                UserManagement userManager = SimplePets.getUserManager();
                Bukkit.getOnlinePlayers().forEach(userManager::getPetUser);
            }
        }.runTaskLater(this, 20);

    }

    private void outputTimings() {
        File file = new File(getDataFolder(), "Timings.json");
        if (file.exists()) file.delete();
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write(TaskTimer.fetchAllCompletedTimers().toString(WriterConfig.PRETTY_PRINT));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        isStarting = false;
        outputTimings();
        supportedVersions.clear();
        if (petUtilities == null) return; // Failed to load this field due to unsupported version
        SimplePets.getDebugLogger().debug(DebugLevel.NORMAL, "Saving player pets (if there are any)", false);
        if (USER_MANAGER != null)
            USER_MANAGER.getAllUsers().forEach(user -> {
                if (user.getPlayer() != null) {
                    ((PetOwner) user).markForRespawn();
                }
            });

        DebugCommand.fetchDebug(json -> {
            json.set("reloaded", !isShuttingDown());
            DebugCommand.log(getDataFolder(), "debug.json", json.toString(WriterConfig.PRETTY_PRINT));
            SimplePets.getDebugLogger().debug(DebugLevel.DEBUG, "Generated debug information while disabling", false);
        }, true);

        petUtilities = null;
        USER_MANAGER = null;
        PET_CONFIG = null;
        SPAWN_UTIL = null;

        // Detected a reload...
        if (!isShuttingDown()) {
            SimplePets.getDebugLogger().debug(DebugBuilder.build().setMessages(
                    "------------------------------------",
                    "    The plugin has detected a reload",
                    "If you encounter ANY strange issues then this will be the cause.",
                    "To fix those, Simply RESTART your server",
                    "------------------------------------"
            ).setSync(false).setBroadcast(true).setLevel(DebugLevel.CRITICAL));
            ConfigOption.INSTANCE.RELOAD_DETECT.setValue(true, true);
        }

        configuration = null;
        if (addonManager != null) addonManager.cleanup();
        addonManager = null;
        debug = null;
        fullyStarted = false;
    }

    private boolean checkJavaVersion() {
        if (ServerVersion.isEqualNew(ServerVersion.v1_18)
                && (!JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17))) {
            debug.debug(DebugBuilder.build(getClass())
                    .setLevel(DebugLevel.CRITICAL)
                    .setBroadcast(true)
                    .setMessages(
                            "Your server does not support Java 17!",
                            "Java 17 is required for servers 1.18+ (Mojang Requirement)",
                            "Disabling the plugin..."
                    )
            );
            return false;
        }

        if (ServerVersion.isEqualNew(ServerVersion.v1_17)
                && ServerVersion.isOlder(ServerVersion.v1_18)
                && (!JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_16))) {
            debug.debug(DebugBuilder.build(getClass())
                    .setLevel(DebugLevel.CRITICAL)
                    .setBroadcast(true)
                    .setMessages(
                            "Your server does not support Java 16!",
                            "Java 16 is required for servers 1.17-1.17.1 (Mojang Requirement)",
                            "Disabling the plugin..."
                    )
            );
            return false;
        }
        return true;
    }

    private void handleUpdateUtils() {
        if (!ConfigOption.INSTANCE.UPDATE_CHECK_ENABLED.getValue()) return;
        int time = ConfigOption.INSTANCE.UPDATE_CHECK_TIME.getValue();
        TimeUnit unit;

        String timeunit = ConfigOption.INSTANCE.UPDATE_CHECK_UNIT.getValue();
        try {
            unit = TimeUnit.valueOf(timeunit);
        } catch (Exception e) {
            unit = TimeUnit.HOURS;
            debug.debug(DebugLevel.ERROR, "Could not find unit for '" + timeunit + "'");
        }

        debug.debug(DebugLevel.HIDDEN, "Initializing update checker");
        if ((Premium.getDownloadType() == Premium.DownloadType.JENKINS) || ConfigOption.INSTANCE.UPDATE_CHECK_DEV_BUILDS.getValue()) {
            updateResult = new UpdateResult().setPreStart(() -> debug.debug(DebugLevel.UPDATE, "Checking for new builds..."))
                    .setFailParse(members -> debug.debug(DebugLevel.UPDATE, "Data collected: " + members.toString(WriterConfig.PRETTY_PRINT)))
                    .setNoNewBuilds(() -> debug.debug(DebugLevel.UPDATE, "No new builds were found"))
                    .setOnError(() -> debug.debug(DebugLevel.UPDATE, "An error occurred when checking for an update"))
                    .setNewBuild(members -> {
                        int latestBuild = members.getInt("build", -1);

                        // New build found
                        if (latestBuild > updateResult.getCurrentBuild()) {
                            debug.debug(DebugLevel.UPDATE, "You are " + (latestBuild - updateResult.getCurrentBuild()) + " build(s) behind the latest.");
                            debug.debug(DebugLevel.UPDATE, "https://ci.pluginwiki.us/job/" + updateResult.getRepo() + "/" + latestBuild + "/");
                        }
                    });
            updateUtils = new UpdateUtils(this, updateResult);
            updateUtils.startUpdateTask(time, unit); // Runs the update check every 12 hours
        }
        if (Premium.getDownloadType() != Premium.DownloadType.JENKINS) {
            int resourceID = Integer.parseInt(Premium.RESOURCE_ID);
            new UpdateChecker(this, Premium.getDownloadType().toSource(), Premium.RESOURCE_ID)
                    .setChangelogLink(resourceID)
                    .setDownloadLink(resourceID)
                    .setColoredConsoleOutput(true)
                    .setNotifyOpsOnJoin(true).setNotifyByPermissionOnJoin("pet.update")
                    .suppressUpToDateMessage(true)
                    .checkEveryXHours(12)
                    .checkNow();
        }
    }

    @Override
    public boolean isPremiumJar() {
        return Premium.isPremium();
    }

    public boolean wasReloaded() {
        return reloaded;
    }

    private boolean isShuttingDown() {
        try {
            Method isStopping = Bukkit.class.getDeclaredMethod("isStopping");
            return (boolean) Reflection.invoke(isStopping, null);
        } catch (Exception e) {
            Class<?> nmsClass = Reflection.getNmsClass("MinecraftServer", "server");
            try {
                Object server = Reflection.getMethod(nmsClass, "getServer").invoke(null);
                Method isRunning = Reflection.getMethod(nmsClass, new String[]{"isRunning", "ab"});
                return !((boolean) Reflection.invoke(isRunning, server));
            } catch (IllegalAccessException | InvocationTargetException exception) {
                exception.printStackTrace();
            }
        }
        return true;
    }

    private void handleManagers() {
        debug.debug(DebugLevel.HIDDEN, "Initializing plugin managers");
        particleManager = new ParticleManager(this);
        renameManager = new RenameManager(this);
        PET_CONFIG = new PetConfiguration(this);
        USER_MANAGER = new UserManager(this);

        itemManager = new ItemManager();
        itemManager.initiate();

        inventoryManager = new InventoryManager();
        inventoryManager.initiate();
    }

    // Registers all listeners
    private void handleListeners() {
        debug.debug(DebugLevel.HIDDEN, "Registering plugin listeners");

        PluginManager manager = Bukkit.getPluginManager();
        if (ConfigOption.INSTANCE.PET_TOGGLES_SPAWN_BYPASS.getValue())
            manager.registerEvents(new PetSpawnListener(), this);
        manager.registerEvents(new AddonGUIListener(), this);
        manager.registerEvents(new ChunkUnloadListener(), this);
        manager.registerEvents(new DamageListener(), this);
        manager.registerEvents(new DataGUIListener(), this);
        manager.registerEvents(new InteractListener(), this);
        manager.registerEvents(new JoinLeaveListeners(), this);
        manager.registerEvents(new PetEventListener(), this);
        manager.registerEvents(new PetSelectorGUIListener(), this);
        manager.registerEvents(new SavesGUIListener(), this);
        manager.registerEvents(new SelectionGUIListener(), this);
        manager.registerEvents(new LocationChangeListener(), this);
    }

    @Override
    public ISpawnUtil getSpawnUtil() {
        return SPAWN_UTIL;
    }

    @Override
    public PetConfigManager getPetConfigManager() {
        return PET_CONFIG;
    }

    public void reloadPetConfigManager() {
        PET_CONFIG = new PetConfiguration(this);
    }

    @Override
    public ItemHandler getItemHandler() {
        return itemManager;
    }

    @Override
    public GUIHandler getGUIHandler() {
        return inventoryManager;
    }

    @Override
    public UserManagement getUserManager() {
        return USER_MANAGER;
    }


    public double getJavaVersion() {
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

    // Checks if the server is using WorldGuard and fetches the value of 'mobs.block-plugin-spawning'
    public void checkWorldGuard(Consumer<Boolean> consumer) {
        Plugin worldguard = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (worldguard == null) return;
        FileConfiguration config = worldguard.getConfig();
        consumer.accept(config.getBoolean("mobs.block-plugin-spawning", false));
    }

    @Override
    public Config getConfiguration() {
        return configuration;
    }

    @Override
    public IPetUtilities getPetUtilities() {
        return petUtilities;
    }

    private Map<String, Integer> getActivePets() {
        Map<String, Integer> users = new HashMap<>();

        SimplePets.getUserManager().getAllUsers().forEach(user -> {
            user.getPetEntities().forEach(entityPet -> {
                PetType type = entityPet.getPetType();

                String name = type.getName();
                if (!users.containsKey(name)) {
                    users.put(name, 1);
                } else {
                    users.put(name, users.get(name) + 1);
                }
            });
        });

        return users;
    }

    private Map<String, Integer> getSpawnedPetCounts() {
        Map<String, Integer> users = new HashMap<>();
        getSpawnUtil().getSpawnCount().forEach((petType, integer) -> {
            users.put(petType.getName(), integer);
        });
        return users;
    }

    public AddonManager getAddonManager() {
        return addonManager;
    }

    private void handleMetrics() {
        SimplePets.getDebugLogger().debug(DebugLevel.HIDDEN, "Loading Metrics");
        Metrics metrics = new Metrics(this);
        metrics.addCustomChart(new Metrics.SimplePie("stupid_config_option_for_gui_command", () -> String.valueOf(ConfigOption.INSTANCE.SIMPLER_GUI.getValue())));
        metrics.addCustomChart(new Metrics.AdvancedPie("spawned_pet_counter", this::getSpawnedPetCounts));
        metrics.addCustomChart(new Metrics.AdvancedPie("active_pets", this::getActivePets));
        metrics.addCustomChart(new Metrics.DrilldownPie("loaded_addons", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();

            Map<String, Integer> entry = new HashMap<>();
            entry.put("addon", 1);
            addonManager.getLoadedAddons().forEach(addon -> {
                if (addonManager.getRegisteredAddons().contains(addon.getNamespace().namespace()))
                    map.put(addon.getNamespace().namespace(), entry);
            });
            return map;
        }));
        metrics.addCustomChart(new Metrics.DrilldownPie("download_type", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Map<String, Integer> entry = new HashMap<>();
            entry.put("download_type", 1);
            map.put(Premium.getDownloadType().name(), entry);
            return map;
        }));
        metrics.addCustomChart(new Metrics.AdvancedPie("addon_tracker", () -> {
            Map<String, Integer> valueMap = new HashMap<>();

            int custom = 0;
            int registered = 0;
            for (AddonLocalData localData : addonManager.getLocalDataMap().keySet()) {
                if (addonManager.getRegisteredAddons().contains(localData.getName())) {
                    registered++;
                } else {
                    custom++;
                }
            }

            valueMap.put("Registered Addons", registered);
            valueMap.put("Custom Addons", custom);
            return valueMap;
        }));
    }

    private void reloadSpawner() {
        ServerVersion version = ServerVersion.getVersion();
        try {
            Class<?> clazz = Class.forName("simplepets.brainsynder.versions." + version.name() + ".SpawnerUtil");
            if (clazz == null) return;
            if (ISpawnUtil.class.isAssignableFrom(clazz)) {
                SPAWN_UTIL = (ISpawnUtil) clazz.getConstructor().newInstance();
                debug.debug(DebugLevel.HIDDEN, "Successfully Linked to " + version.name() + " SpawnUtil Class");
            }
        } catch (Exception e) {
            debug.debug(DebugBuilder.build(getClass())
                    .setLevel(DebugLevel.CRITICAL)
                    .setBroadcast(true)
                    .setMessages(
                            "OH NO! We could not find any support for your servers version " + ServerVersion.getVersion().name().replace("v", "").replace("_", "."),
                            "Please check the Jenkins for an updated build: https://ci.pluginwiki.us/job/SimplePets_v5/",
                            "Check if there is a SimplePets-" + ServerVersion.getVersion().name().replace("v", "").replace("_", ".") + ".jar (IF AVAILABLE)"
                    )
            );
        }
    }

    public RenameManager getRenameManager() {
        return renameManager;
    }

    public File getItemFolder() {
        return itemFolder;
    }

    @Override
    public ParticleManager getParticleHandler() {
        return particleManager;
    }

    @Override
    public DebugLogger getDebugLogger() {
        return debug;
    }

    public static PetCore getInstance() {
        return instance;
    }

    public UpdateUtils getUpdateUtils() {
        return updateUtils;
    }


    private boolean fetchSupportedVersions() {
        if (!supportedVersions.isEmpty()) return supportedVersions.contains(ServerVersion.getVersion().name());
        taskTimer.label("Looking for Supported Versions...");
        supportedVersions.clear();
        String current = ServerVersion.getVersion().name();
        boolean supported = false;
        String packageName = "simplepets.brainsynder.versions.<VER>.SpawnerUtil";
        for (ServerVersion version : ServerVersion.values()) {
            if (version.name().equals(current) && (!supported)) supported = true;
            try {
                Class<?> clazz = Class.forName(packageName.replace("<VER>", version.name()), false, getClassLoader());
                if (clazz != null) supportedVersions.add(version.name());
            } catch (Exception ignored) {
            }
        }
        if (!supported) {
            try {
                Class<?> clazz = Class.forName(packageName.replace("<VER>", current), false, getClassLoader());
                if (clazz != null) supportedVersions.add(current);
            } catch (Exception ignored) {
            }
        }

        if (!supportedVersions.contains(current)) {
            taskTimer.label("Finished looking for supported versions.");
            return false;
        }

        if (!supportedVersions.isEmpty()) {
            debug.debug("Found support for version(s): " + supportedVersions.toString().replace("v", "").replace("_", "."));
            debug.debug("Targeting version: " + ServerVersion.getVersion().name().replace("v", "").replace("_", "."));
        }
        taskTimer.label("Finished looking for supported versions.");
        return supported;
    }

    public boolean hasFullyStarted() {
        return fullyStarted;
    }

    @Override
    public boolean isStarting() {
        return isStarting;
    }
}
