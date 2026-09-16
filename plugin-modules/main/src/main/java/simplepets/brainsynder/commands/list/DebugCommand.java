package simplepets.brainsynder.commands.list;

import org.bsdevelopment.pluginutils.PluginUtilities;
import org.bsdevelopment.pluginutils.command.CommandBuilder;
import org.bsdevelopment.pluginutils.libs.json.JsonArray;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bsdevelopment.pluginutils.libs.json.WriterConfig;
import org.bsdevelopment.pluginutils.utilities.PasteClient;
import org.bsdevelopment.pluginutils.utilities.WebConnector;
import org.bsdevelopment.pluginutils.version.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.IPetConfig;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.plugin.config.MessageOption;
import simplepets.brainsynder.api.plugin.config.internal.ConfigEntry;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.commands.PetCommandClass;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.utils.Premium;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

public class DebugCommand implements PetCommandClass {

    private static final DateTimeFormatter DEBUG_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd | HH:mm:ss:SSS");

    @Override
    public CommandBuilder build() {
        return CommandBuilder.create("debug")
                .withPermission("pet.commands.debug")
                .withDescription("Generates debug information")
                .withSubcommand(buildSkipCommand())
                .withSubcommand(buildPetCommand())
                .executes((sender, args) -> {
                    sender.sendMessage(prefix() + " §7Fetching Debug Information...");
                    fetchDebug(json -> uploadDebugResult(sender, json), false);
                });
    }

    public CommandBuilder buildSkipCommand() {
        return CommandBuilder.create("skip")
                .withPermission("pet.commands.debug")
                .withDescription("Generates debug information without Jenkins check")
                .executes((sender, args) -> {
                    sender.sendMessage(prefix() + " §7Fetching Debug Information...");
                    fetchDebug(json -> uploadDebugResult(sender, json), true);
                });
    }

    public CommandBuilder buildPetCommand() {
        return CommandBuilder.create("pet")
                .withPermission("pet.commands.debug")
                .withDescription("Generates debug information for your spawned pets")
                .withRequirement(sender -> sender instanceof Player)
                .executesPlayer((player, args) -> {
                    PetUser user = SimplePets.getUserManager().getPetUser(player).get();
                    JsonObject json = new JsonObject();
                    json.set("uuid", player.getUniqueId().toString());
                    json.set("username", player.getName());
                    json.set("number-of-pets-spawned", user.getPetEntities().size());

                    JsonArray petArray = new JsonArray();
                    user.getPetEntities().forEach(entityPet -> {
                        JsonObject object = new JsonObject();
                        entityPet.fetchPetDebugInformation(object);
                        petArray.add(object);
                    });
                    json.set("pets", petArray);

                    log(new File(PetCore.getInstance().getDataFolder() + File.separator + "PlayerDebug"),
                            player.getUniqueId() + ".json", json.toString(WriterConfig.PRETTY_PRINT));
                    player.sendMessage(prefix() + " §7Generated §e'plugins/SimplePets/PlayerDebug/" + player.getUniqueId() + ".json'");
                });
    }

    private static void uploadDebugResult(CommandSender sender, JsonObject json) {
        String content = json.toString(WriterConfig.PRETTY_PRINT);
        log(PetCore.getInstance().getDataFolder(), "debug.json", content);
        sender.sendMessage(prefix() + " §7Generated §e'plugins/SimplePets/debug.json'");
        PluginUtilities.getScheduler().runTaskAsynchronously(() -> {
            try {
                String url = PasteClient.pasteUrl(PasteClient.upload(content, "json"));
                PluginUtilities.getScheduler().runTask(() -> sender.sendMessage(prefix() + " §7Uploaded to PasteLog:§e " + url));
            } catch (Exception e) {
                PluginUtilities.getScheduler().runTask(() -> sender.sendMessage(prefix() + " §cFailed to upload debug: " + e.getMessage()));
            }
        });
    }

    public static void fetchDebug(Consumer<JsonObject> consumer, boolean skipJenkins) {
        JsonObject json = new JsonObject();
        json.add("premium_purchase", Premium.isPremium());
        json.add("reloaded", PetCore.getInstance().wasReloaded());
        PetCore.getInstance().checkWorldGuard(value -> json.add("worldguard_config_check", value));
        json.add("server", fetchServerInfo());

        fetchJenkinsInfo(skipJenkins, jenkinsResult -> {
            if (!skipJenkins) json.add("jenkins", jenkinsResult);
            json.add("plugins", fetchPlugins());
            json.add("loaded_addons", fetchAddons());
            json.add("debug_log", fetchDebugMessages());
            json.add("was_reloaded", PetCore.getInstance().wasReloaded());
            json.add("config", fetchConfigValues());
            json.add("pet_types", fetchPetTypeStatus());

            consumer.accept(json);
        });
    }

    private static JsonObject fetchPetTypeStatus() {
        JsonArray available = new JsonArray();
        JsonArray disabled = new JsonArray();
        JsonArray notSupported = new JsonArray();
        JsonArray notRegistered = new JsonArray();
        JsonArray inDevelopment = new JsonArray();

        for (PetType type : PetType.values()) {
            if (type == PetType.UNKNOWN) continue;
            Optional<IPetConfig> config = SimplePets.getPetConfigManager().getPetConfig(type);

            if (config.isPresent() && !config.get().isEnabled()) {
                disabled.add(type.getName());
            } else if (!type.isSupported()) {
                notSupported.add(type.getName());
            } else if (!SimplePets.getSpawnUtil().isRegistered(type)) {
                notRegistered.add(type.getName());
            } else if (type.isInDevelopment()) {
                inDevelopment.add(type.getName());
            } else {
                available.add(type.getName());
            }
        }

        JsonObject result = new JsonObject();
        result.add("available", available);
        if (!disabled.isEmpty()) result.add("disabled", disabled);
        if (!notSupported.isEmpty()) result.add("not_supported", notSupported);
        if (!notRegistered.isEmpty()) result.add("not_registered", notRegistered);
        if (!inDevelopment.isEmpty()) result.add("in_development", inDevelopment);
        return result;
    }

    private static JsonObject fetchConfigValues() {
        JsonObject config = new JsonObject();
        Set<String> sensitiveKeys = Set.of(
                ConfigOption.MYSQL_PASSWORD.path(),
                ConfigOption.MYSQL_USERNAME.path(),
                ConfigOption.MYSQL_HOST.path()
        );

        for (ConfigEntry<?> entry : ConfigOption.REGISTRY.all()) {
            String path = entry.path();
            if (sensitiveKeys.contains(path)) {
                config.add(path, "[REDACTED]");
                continue;
            }
            Object value = entry.get();
            if (value instanceof Boolean b) {
                config.add(path, b);
            } else if (value instanceof Integer i) {
                config.add(path, i);
            } else if (value instanceof Double d) {
                config.add(path, d);
            } else if (value instanceof List<?> list) {
                JsonArray arr = new JsonArray();
                list.forEach(item -> arr.add(String.valueOf(item)));
                config.add(path, arr);
            } else {
                config.add(path, String.valueOf(value));
            }
        }
        return config;
    }

    private static JsonArray fetchAddons() {
        JsonArray addons = new JsonArray();
        PetCore.getInstance().getAddonManager().getLocalDataMap().forEach((localData, modules) -> {
            JsonObject addonJson = new JsonObject();
            JsonArray moduleArray = new JsonArray();
            modules.forEach(module -> moduleArray.add(
                    "Module: '" + module.getNamespace().namespace() + "' | Loaded: " + module.isLoaded() + " | Enabled: " + module.isEnabled()
            ));
            addonJson.add("addon-name", localData.getName() + "(v" + localData.getVersion() + ") by: " + localData.getAuthors().toString()
                    .replace("[", "").replace("]", ""));
            addonJson.add("addon-file-name", localData.getFile().getName());
            addonJson.add("addon-modules", moduleArray);
            addons.add(addonJson);
        });
        return addons;
    }

    private static JsonArray fetchDebugMessages() {
        LinkedList<DebugBuilder> debugLog = SimplePets.getDebugLogger().getDebugLog();
        JsonArray array = new JsonArray();
        while (!debugLog.isEmpty()) {
            DebugBuilder builder = debugLog.pollFirst();
            ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(builder.timestamp), ZoneOffset.UTC);

            JsonArray messages = new JsonArray();
            builder.getMessages().forEach(messages::add);

            JsonObject entry = new JsonObject();
            entry.add("time/date", DEBUG_TIME_FORMAT.format(zdt));
            entry.add("level", builder.getLevel().getName());
            entry.add("message", messages);
            if (builder.getCaller() != null) entry.add("caller", builder.getCaller());
            array.add(entry);
        }
        return array;
    }

    private static JsonObject fetchServerInfo() {
        var si = PluginUtilities.getServerInformation();
        return new JsonObject()
                .add("java", si.getJavaVersion())
                .add("server-information", new JsonObject()
                        .add("server-type", si.getServerType())
                        .add("minecraft-version", si.getMinecraftVersion())
                        .add("server-build", si.getBuildVersion())
                        .add("bukkit-version", si.getBukkitVersion())
                        .add("raw-version", si.getRawVersion())
                        .add("paper", si.isPaper())
                        .add("mojang-mapped", si.isMojangMapped())
                )
                .add("server-version", new JsonObject()
                        .add("nms", ServerVersion.getVersion().getSpigotNMS())
                        .add("name", ServerVersion.getVersion().getVersionName())
                )
                .add("simplepets", new JsonObject()
                        .add("version", PetCore.getInstance().getDescription().getVersion())
                        .add("legacy-pathfinding", ConfigOption.LEGACY_PATHFINDING_ENABLED.get())
                );
    }

    private static void fetchJenkinsInfo(boolean skipJenkins, Consumer<JsonObject> consumer) {
        if (skipJenkins) {
            consumer.accept(new JsonObject());
            return;
        }

        Properties prop = new Properties();
        try {
            prop.load(PetCore.getInstance().getClass().getResourceAsStream("/plugin.properties"));
        } catch (IOException ignored) {
        }
        int build = Integer.parseInt(String.valueOf(prop.getOrDefault("build", -1)));

        WebConnector.getInputStreamString("https://builds.bsdevelopment.org/job/SimplePets/lastSuccessfulBuild/buildNumber",
                string -> consumer.accept(parseJenkinsResponse(build, string)));
    }

    private static JsonObject parseJenkinsResponse(int build, String string) {
        JsonObject jenkins = new JsonObject();
        jenkins.add("plugin_build_number", build);
        try {
            int latestBuild = Integer.parseInt(string);
            jenkins.add("jenkins_build_number", latestBuild);
            if (latestBuild > build) jenkins.add("number_of_builds_behind", latestBuild - build);
            if (build > latestBuild) jenkins.add("number_of_builds_behind", "From The Future :O");
        } catch (Exception e) {
            jenkins.add("error_parsing_buildnumber", e.getMessage());
            jenkins.add("raw_response", string);
        }
        return jenkins;
    }

    private static JsonArray fetchPlugins() {
        JsonArray array = new JsonArray();
        Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .filter(plugin -> plugin.isEnabled())
                .map(plugin -> plugin.getDescription().getName() + " (" + plugin.getDescription().getVersion() + ")")
                .sorted()
                .forEach(array::add);
        return array;
    }

    private static String prefix() {
        return PetCore.getInstance().getMessageFile().getTranslation(MessageOption.PREFIX);
    }

    public static void log(File folder, String fileName, String message) {
        try {
            if (!folder.exists()) folder.mkdirs();
            File saveTo = new File(folder, fileName);
            if (saveTo.exists()) saveTo.delete();
            saveTo.createNewFile();

            FileWriter fw = new FileWriter(saveTo, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(message);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
