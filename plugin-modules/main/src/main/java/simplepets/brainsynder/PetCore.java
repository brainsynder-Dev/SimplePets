package simplepets.brainsynder;

import com.jeff_media.updatechecker.UpdateChecker;
import org.bsdevelopment.pluginutils.PluginUtilities;
import org.bsdevelopment.pluginutils.command.CommandBuilder;
import org.bsdevelopment.pluginutils.command.help.HelpCommand;
import org.bsdevelopment.pluginutils.libs.json.Json;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bsdevelopment.pluginutils.libs.json.JsonValue;
import org.bsdevelopment.pluginutils.libs.json.WriterConfig;
import org.bsdevelopment.pluginutils.reflection.Reflection;
import org.bsdevelopment.pluginutils.version.ServerVersion;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.AdvancedPie;
import org.bstats.charts.DrilldownPie;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import simplepets.brainsynder.addon.AddonLocalData;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.inventory.handler.GUIHandler;
import simplepets.brainsynder.api.inventory.handler.ItemHandler;
import simplepets.brainsynder.api.pet.PetConfigManager;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.IPetsPlugin;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.plugin.utils.HelperUtilities;
import simplepets.brainsynder.api.plugin.utils.IPetUtilities;
import simplepets.brainsynder.api.user.UserManagement;
import simplepets.brainsynder.commands.list.*;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.debug.DebugLogger;
import simplepets.brainsynder.files.ConfigFile;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.impl.PetConfiguration;
import simplepets.brainsynder.impl.PetOwner;
import simplepets.brainsynder.impl.PetUtility;
import simplepets.brainsynder.listeners.*;
import simplepets.brainsynder.listeners.breaking.DismountListener;
import simplepets.brainsynder.managers.*;
import simplepets.brainsynder.sql.SQLData;
import simplepets.brainsynder.sql.SQLHandler;
import simplepets.brainsynder.sql.handlers.MySQLHandler;
import simplepets.brainsynder.sql.handlers.SQLiteHandler;
import simplepets.brainsynder.utils.JavaVersion;
import simplepets.brainsynder.utils.Premium;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.utils.VersionFields;
import simplepets.brainsynder.utils.debug.Debug;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PetCore extends JavaPlugin implements IPetsPlugin {
    private boolean versionDetectionDone = false;
    private static PetCore instance;

    private File itemFolder;
    private boolean reloaded = false;
    private boolean fullyStarted = false;
    private boolean isStarting = false;

    private ConfigFile configuration;
    private MessageFile messageFile;

    private ISpawnUtil SPAWN_UTIL;
    private UserManager USER_MANAGER;
    private PetConfiguration PET_CONFIG;
    private RenameManager renameManager;
    private ItemManager itemManager;
    private InventoryManager inventoryManager;
    private ParticleManager particleManager;
    private AddonManager addonManager;

    private Class<?> spawnutilClass = null;
    private String targetVersion = null;

    private Debug debug;
    private IPetUtilities petUtilities;
    private SQLHandler sqlHandler;

    public final Executor sync = task -> PluginUtilities.getScheduler().runTask(task);
    public final Executor async = task -> PluginUtilities.getScheduler().runTaskAsynchronously(task);

    @Override
    public void onEnable() {
        instance = this;
        SimplePets.setPLUGIN(this);
        PluginUtilities.initialize(this);
        isStarting = true;

        debug = new Debug(this);

        if (ServerVersion.getVersion().isEqualOrNewer(ServerVersion.v26_3)) {
            SimplePets.getDebugLogger().debug(DebugBuilder.build()
                .setLevel(DebugLevel.WARNING).setBroadcast(true)
                .setMessages(
                    " *** This version is still under development any issues found please report",
                    " *** On the Github: https://tiny.bsdevelopment.org/pet-issues"
                ));
        }

        if (!checkJavaVersion()) {
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
                    "OH NO! We could not find any support for your servers version " + ServerVersion.getVersion().getVersionName().replace("v", "").replace("_", "."),
                    "Please check the BuildServer for an updated build: https://builds.bsdevelopment.org/jobs/SimplePets"
                )
            );
            isStarting = false;
            return;
        }
        debug.debug(DebugLevel.HIDDEN, "Setting API instance");
        petUtilities = new PetUtility();

        itemFolder = new File(getDataFolder() + File.separator + "Items");

        debug.debug(DebugLevel.HIDDEN, "Initializing Message file");
        messageFile = new MessageFile(getDataFolder());
        messageFile.initValues();

        debug.debug(DebugLevel.HIDDEN, "Initializing Config file");
        configuration = new ConfigFile(this);
        configuration.initValues();

        reloaded = ConfigOption.RELOAD_DETECT.get();
        ConfigOption.RELOAD_DETECT.set(false, true);
        debug.debug(DebugLevel.HIDDEN, "Plugin reloaded: " + reloaded);

        if (ConfigOption.LEGACY_PATHFINDING_ENABLED.get()) {
            SimplePets.getDebugLogger().debug(DebugBuilder.build()
                    .setLevel(DebugLevel.WARNING).setBroadcast(true)
                    .setMessages(
                            " ",
                            " *** NOTICE: Legacy Pathfinding is currently enabled",
                            " *** No support will be given if this is enabled and an issue occurs pertaining to it",
                            " "
                    ));
        }

        reloadSpawner();
        handleManagers();

        debug.debug(DebugLevel.HIDDEN, "Initializing SQL Handler");
        if (SQLData.USE_SQLITE) {
            sqlHandler = new SQLiteHandler();
        } else {
            sqlHandler = new MySQLHandler();
        }
        sqlHandler.initiateDatabase();
        addonManager = new AddonManager(PetCore.this);
        addonManager.initialize();

        try {
            debug.debug(DebugLevel.HIDDEN, "Registering commands");
            CommandBuilder coreCommand = CommandBuilder.create("pet")
                    .withAliases("pets", "simplepets", "simplepet")
                    .withDescription("This is the main command for the plugin")
                    .withSubcommand(new AddonCommand().build())
                    .withSubcommand(new DatabaseCommand().build())
                    .withSubcommand(new DataCommand().build())
                    .withSubcommand(new DebugCommand().build())
                    .withSubcommand(new GUICommand().build())
                    .withSubcommand(new ListCommand().build())
                    .withSubcommand(new ModifyCommand().build())
                    .withSubcommand(new PermissionsCommand().build())
                    .withSubcommand(new PetConfigCommand().build())
                    .withSubcommand(new PurchasedCommand().build())
                    .withSubcommand(new RegenerateCommand().build())
                    .withSubcommand(new ReloadCommand().build())
                    .withSubcommand(new RemoveCommand().build())
                    .withSubcommand(new SummonCommand().build())
                    .executesPlayer((player, args) -> {
                        if (ConfigOption.SIMPLER_GUI.get()) {
                            player.performCommand("pet gui");
                        }
                    });

            if (ConfigOption.RENAME_ENABLED.get())
                coreCommand.withSubcommand(new RenameCommand().build());
            if (Premium.isPremium())
                coreCommand.withSubcommand(new PremiumCommand().build());

            coreCommand.withSubcommand(HelpCommand.of(coreCommand).withPageSize(10).withFormatter(HelpCommand.tellrawFormatter()).build());
            coreCommand.register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        handleListeners();
        handleUpdateUtils();

        Plugin papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (papi != null && papi.isEnabled()) {
            new simplepets.brainsynder.hooks.PlaceholderAPIHook().register();
            debug.debug(DebugLevel.HIDDEN, "Hooked into PlaceholderAPI");
        }

        {
            TimeUnit unit;

            String timeunit = ConfigOption.ADDON_LOAD_UNIT.get();
            try {
                unit = TimeUnit.valueOf(timeunit);
            } catch (Exception e) {
                unit = TimeUnit.SECONDS;
                debug.debug(DebugLevel.ERROR, "Could not find unit for '" + timeunit + "'");
            }

            debug.debug(SimplePets.ADDON, "Loading addons in '" + ConfigOption.ADDON_LOAD_TIME.get() + " " + timeunit + "'");

            PluginUtilities.getScheduler().runTaskLater(() -> {
                addonManager.checkAddons();

                handleMetrics();
            }, Utilities.toTicks(ConfigOption.ADDON_LOAD_TIME.get(), unit));
        }

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

        fullyStarted = true;

        if (Bukkit.getOnlinePlayers().isEmpty()) return;
        // Delay it for a second to actually have the database load
        PluginUtilities.getScheduler().runTaskLater(() -> {
            debug.debug(DebugLevel.HIDDEN, "Respawning pets for players (if there are any)");
            UserManagement userManager = SimplePets.getUserManager();
            Bukkit.getOnlinePlayers().forEach(userManager::getPetUser);
        }, 20);

    }

    @Override
    public void onDisable() {
        isStarting = false;
        versionDetectionDone = false;
        spawnutilClass = null;
        targetVersion = null;
        if (petUtilities == null) return; // Failed to load this field due to unsupported version
        SimplePets.getDebugLogger().debug(DebugLevel.NORMAL, "Saving player pets (if there are any)", false);
        if (USER_MANAGER != null)
            USER_MANAGER.getAllUsers().forEach(user -> {
                if (user.getPlayer() != null) {
                    ((PetOwner) user).markForRespawn();
                }
            });

        DebugCommand.fetchDebug(json -> {
            json.set("reloaded", wasPluginReloaded());
            DebugCommand.log(getDataFolder(), "debug.json", json.toString(WriterConfig.PRETTY_PRINT));
            SimplePets.getDebugLogger().debug(DebugLevel.DEBUG, "Generated debug information while disabling", false);
        }, true);

        petUtilities = null;
        USER_MANAGER = null;
        PET_CONFIG = null;
        SPAWN_UTIL = null;

        // Detected a reload...
        if (wasPluginReloaded()) {
            SimplePets.getDebugLogger().debug(DebugBuilder.build().setMessages(
                "------------------------------------",
                "    The plugin has detected a reload",
                "If you encounter ANY strange issues then this will be the cause.",
                "To fix those, Simply RESTART your server",
                "------------------------------------"
            ).setSync(false).setBroadcast(true).setLevel(DebugLevel.CRITICAL));
            ConfigOption.RELOAD_DETECT.set(true, true);
        }

        configuration = null;
        if (addonManager != null) addonManager.cleanup();
        addonManager = null;
        debug = null;
        fullyStarted = false;
    }

    private boolean checkJavaVersion() {
        if (ServerVersion.getVersion().isEqualOrNewer(ServerVersion.v26_1)
                && (!JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_25))) {
            debug.debug(DebugBuilder.build(getClass())
                    .setLevel(DebugLevel.CRITICAL)
                    .setBroadcast(true)
                    .setMessages(
                            "Your server does not support Java 25!",
                            "Java 25 is required for servers 26.1 -> LATEST (Mojang Requirement)",
                            "Disabling the plugin..."
                    )
            );
            return false;
        }
        if (ServerVersion.getVersion().isEqualOrOlder(ServerVersion.v1_21_11)
                && !JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_21)) {
            debug.debug(DebugBuilder.build(getClass())
                    .setLevel(DebugLevel.CRITICAL)
                    .setBroadcast(true)
                    .setMessages(
                            "Your server does not support Java 21!",
                            "Java 21 is required for servers 1.20.5 -> 1.21.11 (Mojang Requirement)",
                            "Disabling the plugin..."
                    )
            );
            return false;
        }
        return true;
    }

    private void handleUpdateUtils() {
        if (!ConfigOption.UPDATE_CHECK_ENABLED.get()) return;
        debug.debug(DebugLevel.HIDDEN, "Initializing update checker");
        if (Premium.getDownloadType().fromDownloadSite()) {
            try {
                int resourceID = Integer.parseInt(Premium.RESOURCE_ID);
                new UpdateChecker(this, Premium.getDownloadType().toSource(), Premium.RESOURCE_ID)
                        .setChangelogLink(resourceID)
                        .setDownloadLink(resourceID)
                        .setColoredConsoleOutput(true)
                        .setNotifyOpsOnJoin(true).setNotifyByPermissionOnJoin("pet.update")
                        .suppressUpToDateMessage(true)
                        .checkEveryXHours(12)
                        .checkNow();
            }catch (Exception ignored) {}
        }
    }

    public boolean wasReloaded() {
        return reloaded;
    }

    private boolean wasPluginReloaded() {
        try {
            Method isStopping = Bukkit.class.getDeclaredMethod("isStopping");
            return !((boolean) Reflection.executeMethod(isStopping, null));
        } catch (Exception e) {
            String fieldName = VersionFields.fromServerVersion(ServerVersion.getVersion()).getServerRunningField();

            Class<?> nmsClass = Reflection.resolveMinecraftClass("MinecraftServer", "server");

            try {
                Object server = Reflection.resolveMethod(nmsClass, "getServer").invoke(null);
                Field field = nmsClass.getDeclaredField(fieldName);
                Reflection.makeFieldAccessible(field);
                return (boolean) field.get(server);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return false;
    }

    private void handleManagers() {
        debug.debug(DebugLevel.HIDDEN, "Initializing plugin managers");
        migrateLegacyFiles();
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
        if (ConfigOption.PET_TOGGLES_SPAWN_BYPASS.get())
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
        manager.registerEvents(new DismountListener(), this);
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

    public SQLHandler getSqlHandler() {
        return sqlHandler;
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
    public ConfigFile getConfiguration() {
        return configuration;
    }

    public MessageFile getMessageFile() {
        return messageFile;
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
        Metrics metrics = new Metrics(this, 244);
        metrics.addCustomChart(new SimplePie("server_type", () -> String.valueOf(PluginUtilities.getServerInformation().getServerType())));
        metrics.addCustomChart(new SimplePie("stupid_config_option_for_gui_command", () -> String.valueOf(ConfigOption.SIMPLER_GUI.get())));
        metrics.addCustomChart(new AdvancedPie("spawned_pet_counter", this::getSpawnedPetCounts));
        metrics.addCustomChart(new AdvancedPie("active_pets", this::getActivePets));
        metrics.addCustomChart(new DrilldownPie("loaded_addons", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();

            Map<String, Integer> entry = new HashMap<>();
            entry.put("addon", 1);
            addonManager.getLoadedAddons().forEach(addon -> {
                if (addonManager.getRegisteredAddons().contains(addon.getNamespace().namespace()))
                    map.put(addon.getNamespace().namespace(), entry);
            });
            return map;
        }));
        metrics.addCustomChart(new DrilldownPie("download_type", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Map<String, Integer> entry = new HashMap<>();
            entry.put("download_type", 1);
            map.put(Premium.getDownloadType().name(), entry);
            return map;
        }));
        metrics.addCustomChart(new AdvancedPie("addon_tracker", () -> {
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
            if (spawnutilClass == null) return;
            if (ISpawnUtil.class.isAssignableFrom(spawnutilClass)) {
                SPAWN_UTIL = (ISpawnUtil) spawnutilClass.getConstructor(ClassLoader.class, String.class).newInstance(getClassLoader(), targetVersion);
                debug.debug(DebugLevel.HIDDEN, "Successfully Linked to " + version.getVersionName() + " SpawnUtil Class");
            }
        } catch (Exception e) {
            debug.debug(DebugBuilder.build(getClass())
                .setLevel(DebugLevel.CRITICAL)
                .setBroadcast(true)
                .setMessages(
                    "OH NO! We could not find any support for your servers version " + ServerVersion.getVersion().getVersionName().replace("v", "").replace("_", "."),
                    "Please check the Jenkins for an updated build: https://builds.bsdevelopment.org/jobs/SimplePets",
                    "Check the 'Supported Minecraft Versions' section for version support",
                    " ",
                    "Error: " + e.getMessage()
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

    private boolean fetchSupportedVersions() {
        if (versionDetectionDone) return spawnutilClass != null;
        versionDetectionDone = true;

        String current = ServerVersion.getVersion().getVersionName();
        String resolvedVersion = HelperUtilities.resolveTargetVersion("SpawnerUtil");
        if (resolvedVersion == null) return false;

        try {
            spawnutilClass = Class.forName(HelperUtilities.NMS_PATH + "." + resolvedVersion + ".SpawnerUtil", false, getClassLoader());
            targetVersion = resolvedVersion;
        } catch (ClassNotFoundException e) {
            return false;
        }

        if (!resolvedVersion.equals(current)) {
            debug.debug("Version " + current.replace("v", "").replace("_", ".") + " has no dedicated version module, linking to version " + resolvedVersion.replace("v", "").replace("_", "."));
        }

        debug.debug("Targeting version: " + resolvedVersion.replace("v", "").replace("_", "."));
        return true;
    }

    public boolean hasFullyStarted() {
        return fullyStarted;
    }

    @Override
    public boolean isStarting() {
        return isStarting;
    }

    public String getJarName() {
        try {
            Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
            getFileMethod.setAccessible(true);
            File file = (File) getFileMethod.invoke(this);
            return file.getName();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private void migrateLegacyFiles() {
        File petsFolder = new File(getDataFolder(), "Pets");
        File itemsFolder = new File(getDataFolder(), "Items");

        if (petsFolder.exists() && hasLegacyFiles(petsFolder)) {
            debug.debug(DebugBuilder.build().setLevel(DebugLevel.WARNING).setMessages("Legacy pet config files detected — moving to Pets/legacy/ and regenerating"));
            moveFolderContentsToLegacy(petsFolder);
        }

        if (itemsFolder.exists() && hasLegacyFiles(itemsFolder)) {
            debug.debug(DebugBuilder.build().setLevel(DebugLevel.WARNING).setMessages("Legacy item files detected — moving to Items/legacy/ and regenerating"));
            moveFolderContentsToLegacy(itemsFolder);
        }
    }

    private boolean hasLegacyFiles(File folder) {
        File[] files = folder.listFiles(file -> file.isFile() && file.getName().endsWith(".json"));
        if (files == null) return false;

        for (File file : files) {
            try {
                String content = Files.readString(file.toPath());
                JsonValue root = Json.parse(content);
                if (!root.isObject()) continue;

                JsonValue itemValue = root.asObject().get("item");
                if (itemValue == null || !itemValue.isObject()) continue;

                JsonObject item = itemValue.asObject();
                if (item.names().contains("material") && !item.names().contains("id")) return true;
            } catch (IOException ignored) {}
        }
        return false;
    }

    private void moveFolderContentsToLegacy(File folder) {
        File legacyDir = new File(folder, "legacy");
        legacyDir.mkdirs();
        File[] files = folder.listFiles(file -> file.isFile() && file.getName().endsWith(".json"));
        if (files == null) return;

        for (File file : files) {
            file.renameTo(new File(legacyDir, file.getName()));
        }
    }
}
