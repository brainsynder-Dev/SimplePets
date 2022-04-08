package simplepets.brainsynder.managers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lib.brainsynder.files.YamlFile;
import lib.brainsynder.json.Json;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.utils.AdvString;
import lib.brainsynder.web.WebConnector;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.addon.AddonCloudData;
import simplepets.brainsynder.addon.AddonConfig;
import simplepets.brainsynder.addon.AddonLocalData;
import simplepets.brainsynder.addon.PetModule;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.debug.DebugLevel;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AddonManager {
    private final YamlFile addonFile;
    private final PetCore plugin;
    private final File folder;
    private final List<AddonCloudData> cloudAddons;
    private final List<String> registeredAddons;
    private final List<PetModule> rawAddons, loadedAddons;
    private final Map<AddonLocalData, List<PetModule>> localDataMap, tempMap;

    public AddonManager(PetCore plugin) {
        this.plugin = plugin;
        tempMap = Maps.newHashMap();
        localDataMap = Maps.newHashMap();
        cloudAddons = Lists.newArrayList();
        registeredAddons = Lists.newArrayList();
        rawAddons = Lists.newArrayList();
        loadedAddons = Lists.newArrayList();
        folder = new File(plugin.getDataFolder() + File.separator + "Addons");

        addonFile = new YamlFile(plugin.getDataFolder(), "AddonConfig.yml") {
            @Override
            public void loadDefaults() {

            }
        };

        if (!folder.exists()) return;
        for (File file : folder.listFiles()) {
            loadAddon(file);
        }
    }

    public File getFolder() {
        return folder;
    }

    public void loadAddon(File file) {
        if (file.isDirectory()) return;
        if (!file.getName().endsWith(".jar")) return;
        JsonObject information = new JsonObject();

        try {
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement();
                if (zipEntry.getName().equals("addon.json")) {
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    information = (JsonObject) Json.parse(new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (information.isEmpty()) {
            SimplePets.getDebugLogger().debug(DebugLevel.ERROR, file.getName() + " is missing the 'addon.json' file, This addon needs updating to the new required addon format.");
            return;
        }

        AddonLocalData localData = new AddonLocalData(file, information);
        if (!localData.getPluginSupport().isEmpty()) {
            boolean missingSupport = false;
            for (AddonLocalData.SupportData supportData : localData.getPluginSupport()) {
                Plugin plugin = Bukkit.getPluginManager().getPlugin(supportData.name());
                if ((plugin == null) || (!plugin.isEnabled())) {
                    missingSupport = true;
                    continue;
                }
                missingSupport = false;
                break;
            }

            if (missingSupport) {
                SimplePets.getDebugLogger().debug(DebugBuilder.build().setLevel(SimplePets.ADDON).setMessages(
                        "The "+(localData.getName().endsWith("Addon") ? localData.getName() : localData.getName()+"Addon")+" could not find plugin requirements:"
                ));
                localData.getPluginSupport().forEach(supportData -> {
                    SimplePets.getDebugLogger().debug(DebugBuilder.build().setLevel(SimplePets.ADDON).setMessages(
                            " - "+supportData.url()+" ("+supportData.name()+")"
                    ));
                });
                return;
            }
        }

        // This is just a check added as a fail-safe in case they have the plugin but the plugin is missing class(s)
        if (!localData.getClassChecks().isEmpty()) {
            for (String path : localData.getClassChecks()) {
                try {
                    Class.forName(path, false, PetCore.getInstance().getClass().getClassLoader());
                }catch (Exception e) {
                    SimplePets.getDebugLogger().debug(DebugBuilder.build().setLevel(DebugLevel.ERROR).setMessages(
                            "Failed to locate '"+path+"' used for the "+localData.getName()+" addon."
                    ));
                    return;
                }
            }
        }

        try {
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());

            try (JarFile jarFile = new JarFile(file)) {
                Enumeration<JarEntry> e = jarFile.entries();
                List<PetModule> modules = new ArrayList<>();

                while (e.hasMoreElements()) {
                    JarEntry je = e.nextElement();

                    if (je.isDirectory() || !je.getName().endsWith(".class")) continue;
                    String className = je.getName().substring(0, je.getName().length() - 6);
                    className = className.replace('/', '.');
                    Class<?> loadClass = urlClassLoader.loadClass(className);
                    if (!PetModule.class.isAssignableFrom(loadClass)) continue;
                    try {
                        PetModule addon = (PetModule) loadClass.getDeclaredConstructor().newInstance();
                        addon.setLocalData(localData);
                        rawAddons.add(addon);
                        modules.add(addon);
                    }catch (NoSuchMethodException | InvocationTargetException e1) {
                        SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "An error occurred when trying to load a module in the addon: "+localData.getName());
                    }
                }
                localDataMap.putIfAbsent(localData, modules);
                tempMap.put(localData, modules);
            }

            if (rawAddons.isEmpty())
                SimplePets.getDebugLogger().debug(DebugBuilder.build(getClass()).setLevel(SimplePets.ADDON).setMessages(
                        "Could not find a class that extends PetModule",
                        "Are you sure '" + file.getName() + "' is an addon?"
                ));
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            SimplePets.getDebugLogger().debug(DebugBuilder.build(getClass()).setLevel(DebugLevel.ERROR).setMessages(
                    "Failed to load addon: " + file.getName(),
                    "Error Message: " + e.getMessage()
            ));
            e.printStackTrace();
        }
    }

    public void cleanup() {
        for (PetModule addon : loadedAddons) {
            addon.cleanup();
            HandlerList.unregisterAll(addon);
        }

        loadedAddons.clear();
        rawAddons.clear();
    }

    public void initialize() {
        tempMap.forEach((localData, modules) -> {
            if (!isSupported(localData.getSupportedBuild())) {
                SimplePets.getDebugLogger().debug(DebugBuilder.build(getClass()).setLevel(SimplePets.ADDON).setMessages(
                        localData.getName() + " (by " + localData.getAuthors() + ") is not supported for version " + PetCore.getInstance().getDescription().getVersion()
                ));
                return;
            }

            SimplePets.getDebugLogger().debug(SimplePets.ADDON, "Loading modules for the "+localData.getName() + " addon");
            modules.forEach(module -> {
                try {
                    String name = module.getNamespace().namespace();

                    new AddonConfig(new File(folder + File.separator + "configs"), name + ".yml") {
                        @Override
                        public void loadDefaults() {
                            module.loadDefaults(this);
                        }
                    };

                    module.setLoaded(true);

                    if (!this.addonFile.contains(name + ".Enabled")) {
                        this.addonFile.addComment(name + ".Enabled", "Enable/Disable the " + name + " module");
                        this.addonFile.set(name + ".Enabled", true);
                    }
                    loadedAddons.add(module);
                    boolean enabled = this.addonFile.getBoolean(name + ".Enabled", true);
                    module.setAddonFolder(folder);

                    if (module.shouldEnable()) {
                        module.setEnabled(enabled);
                    } else return;


                    if (module.isEnabled()) {
                        Bukkit.getPluginManager().registerEvents(module, plugin);
                        module.init();
                    }
                }catch (Exception e) {
                    SimplePets.getDebugLogger().debug(DebugBuilder.build(getClass()).setLevel(SimplePets.ADDON).setMessages(
                            "Failed to initialize addon module: " + module.getClass().getSimpleName()
                    ));
                    e.printStackTrace();
                }
            });
        });
        rawAddons.clear();
        tempMap.clear();
    }

    public Map<AddonLocalData, List<PetModule>> getLocalDataMap() {
        return localDataMap;
    }

    public List<AddonCloudData> getCloudAddons() {
        return cloudAddons;
    }

    public void downloadViaName (String name, String url, Runnable runnable) {
        CompletableFuture.runAsync(() -> {
            try {
                download(new URL(url), name, file -> {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            loadAddon(file);
                            initialize();
                            runnable.run();
                        }
                    }.runTask(plugin);
                });
            } catch (MalformedURLException e) {
                try {
                    final File file = new File(folder.getAbsolutePath() + "/" + name);
                    file.delete();
                    FileUtils.copyURLToFile(new URL(url), file);
                    runnable.run();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void downloadAddon(File original, String url, Runnable runnable) {
        CompletableFuture.runAsync(() -> {
            try {
                download(new URL(url), original.getName(), file -> {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            loadAddon(file);
                            initialize();
                            runnable.run();
                        }
                    }.runTask(plugin);
                });
            } catch (MalformedURLException e) {
                try {
                    final File file = new File(folder.getAbsolutePath() + "/" + original.getName());
                    original.delete();
                    FileUtils.copyURLToFile(new URL(url), file);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            loadAddon(file);
                            initialize();
                            runnable.run();
                        }
                    }.runTask(plugin);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void toggleAddonModule(PetModule module, boolean enabled) {
        if (enabled && isSupported(module.getLocalData().getSupportedBuild())) {

            new AddonConfig(new File(folder + File.separator + "configs"), module.getNamespace().namespace() + ".yml") {
                @Override
                public void loadDefaults() {
                    module.loadDefaults(this);
                }
            };

            module.init();
            Bukkit.getPluginManager().registerEvents(module, plugin);
        } else {
            HandlerList.unregisterAll(module);
            module.cleanup();
        }
        String name = module.getNamespace().namespace();

        addonFile.set(name + ".Enabled", enabled);
        module.setEnabled(enabled);
    }

    public Optional<AddonLocalData> fetchAddon(String name) {
        for (AddonLocalData localData : localDataMap.keySet()) {
            if (localData.getName().equalsIgnoreCase(name)) return Optional.of(localData);
        }
        return Optional.empty();
    }

    public Optional<AddonCloudData> fetchCloudData(String name) {
        for (AddonCloudData cloudData : cloudAddons) {
            if (cloudData.getName().equalsIgnoreCase(name)) return Optional.of(cloudData);
        }
        return Optional.empty();
    }

    public Optional<PetModule> fetchAddonModule(String name) {
        return fetchAddonModule(null, name);
    }

    public Optional<PetModule> fetchAddonModule(AddonLocalData localData, String name) {
        for (Map.Entry<AddonLocalData, List<PetModule>> entry : localDataMap.entrySet()) {
            if ((localData != null) && (!localData.getName().equals(entry.getKey().getName()))) continue;
            for (PetModule module : entry.getValue())
                if (module.getNamespace().namespace().equalsIgnoreCase(name)) return Optional.of(module);
        }
        return Optional.empty();
    }

    public void update(AddonLocalData localData, String url, Runnable runnable) {
        localDataMap.getOrDefault(localData, Lists.newArrayList()).forEach(module -> {
            module.cleanup();
            HandlerList.unregisterAll(module);
            loadedAddons.remove(module);
        });
        localDataMap.remove(localData);

        downloadAddon(localData.getFile(), url, runnable);
    }

    public boolean isSupported(int build) {
        if (build <= 0) return true;
        String plugin = PetCore.getInstance().getDescription().getVersion().toLowerCase();
        if (!plugin.contains("-build-")) return false; // Custom fork with different version?
        return Integer.parseInt(AdvString.after("-build-", plugin)) >= build;
    }

    public void checkAddons() {
        Map<String, AddonLocalData> addonMap = Maps.newHashMap();
        localDataMap.forEach((localData, modules) -> addonMap.put(localData.getName(), localData));

        fetchAddons(addons -> {
            List<AddonCloudData> updateNeeded = Lists.newArrayList();

            addons.forEach(cloudAddon -> {
                if (addonMap.containsKey(cloudAddon.getName())) {
                    AddonLocalData localData = addonMap.get(cloudAddon.getName());
                    if (cloudAddon.getVersion() > localData.getVersion()) {
                        updateNeeded.add(cloudAddon);
                    }
                }
                cloudAddons.add(cloudAddon);
            });

            if (updateNeeded.isEmpty()) return;
            updateNeeded.forEach(addonData -> {

                SimplePets.getDebugLogger().debug(DebugBuilder.build(getClass()).setLevel(DebugLevel.UPDATE).setMessages(
                        "There is an update for the addon '" + addonData.getName() + "' version " + addonData.getVersion()
                ));
            });
        });
    }

    public List<PetModule> getLoadedAddons() {
        return loadedAddons;
    }

    public void fetchAddons(Consumer<List<AddonCloudData>> consumer) {
        WebConnector.getInputStreamString("https://bsdevelopment.org/addons/addons.json", plugin, result -> {
            List<AddonCloudData> addons = Lists.newArrayList();

            JsonObject jsonObject = (JsonObject) Json.parse(result);
            jsonObject.forEach(member -> {
                JsonObject json = (JsonObject) member.getValue();
                AddonCloudData data = new AddonCloudData(
                        "https://bsdevelopment.org/addons/download/" + member.getName(),
                        member.getName(),
                        json.getString("author", "Unknown"),
                        Double.parseDouble(json.getString("version", "0.0"))
                );

                if (json.names().contains("supportedVersion"))
                    data.setSupportedVersion(json.getString("supportedVersion", "Unknown"));

                if (json.names().contains("description")) {
                    List<String> description = Lists.newArrayList();
                    ((JsonArray) json.get("description")).forEach(value -> description.add(value.asString()));
                    data.setDescription(description);
                }

                addons.add(data);
                registeredAddons.add(data.getName());
            });
            consumer.accept(addons);
        });
    }

    public List<String> getRegisteredAddons() {
        return registeredAddons;
    }

    private void download (URL url, String backup, Consumer<File> fileConsumer) {
        try {
            URLConnection con = url.openConnection();

            String fieldValue = con.getHeaderField("Content-Disposition");
            if (fieldValue == null || !fieldValue.contains("filename=\"")) {
                File file = new File(folder.getAbsolutePath() + "/" + backup);
                file.delete();
                FileUtils.copyURLToFile(url, file);
                fileConsumer.accept(file);
                return;
            }

            // parse the file name from the header field
            String filename = fieldValue.substring(fieldValue.indexOf("filename=\"") + 10, fieldValue.length() - 1);
            // create file in systems temporary directory
            File download = new File(folder, filename);

            // open the stream and download
            ReadableByteChannel rbc = Channels.newChannel(con.getInputStream());
            FileOutputStream fos = new FileOutputStream(download);
            try {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            } finally {
                fos.close();
                fileConsumer.accept(download);
            }
        }catch (Exception e) {}
    }
}
