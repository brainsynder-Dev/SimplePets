package simplepets.brainsynder.commands.list;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.json.Json;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.json.WriterConfig;
import lib.brainsynder.web.WebConnector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.commands.PetsCommand;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
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

@ICommand(
    name = "debug",
    usage = "[skip-jenkins|pet]",
    description = "Generates debug information"
)
@Permission(permission = "debug", adminCommand = true)
public class DebugCommand extends PetSubCommand {
    private final PetsCommand parent;

    public DebugCommand(PetsCommand parent) {
        super(parent.getPlugin());
        this.parent = parent;
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + " §7Fetching Debug Information...");
        boolean skipJenkins = false;

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("pet")) {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(ChatColor.RED + "You must be a player to run this command for yourself.");
                    return;
                }
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

                log(new File(getPlugin().getDataFolder() + File.separator + "PlayerDebug"), player.getUniqueId()+".json", json.toString(WriterConfig.PRETTY_PRINT));
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + " §7Generated §e'plugins/SimplePets/PlayerDebug/"+player.getUniqueId()+".json'");
                return;
            }
            skipJenkins = Boolean.parseBoolean(args[0]);
        }

        fetchDebug(json -> {
            log(getPlugin().getDataFolder(), "debug.json", json.toString(WriterConfig.PRETTY_PRINT));
            sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + " §7Generated §e'plugins/SimplePets/debug.json'");

            WebConnector.uploadPaste(PetCore.getInstance(), json.toString(WriterConfig.PRETTY_PRINT), s -> {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + " §7Uploaded to PasteLog:§e " + s);
            });
        }, skipJenkins);
    }

    public static void fetchDebug(Consumer<JsonObject> consumer, boolean skipJenkins) {
        JsonObject json = new JsonObject();
        json.add("premium_purchase", Premium.isPremium());
        json.add("reloaded", PetCore.getInstance().wasReloaded());
        PetCore.getInstance().checkWorldGuard(value -> json.add("worldguard_config_check", value));
        fetchServerInfo(object -> json.add("server", object));
        fetchJenkinsInfo(skipJenkins, object -> {
            if (!skipJenkins) json.add("jenkins", object);
            json.add("plugins", fetchPlugins());

            JsonArray addons = new JsonArray();
            PetCore.getInstance().getAddonManager().getLocalDataMap().forEach((localData, modules) -> {
                JsonObject addonJson = new JsonObject();
                JsonArray moduleArray = new JsonArray();
                modules.forEach(module -> {
                    moduleArray.add("Module: '" + module.getNamespace().namespace() + "' | Loaded: " + module.isLoaded() + " | Enabled: " + module.isEnabled());
                });

                addonJson.add("addon-name", localData.getName() + "(v" + localData.getVersion() + ") by: " + localData.getAuthors().toString()
                    .replace("[", "").replace("]", ""));
                addonJson.add("addon-file-name", localData.getFile().getName());
                addonJson.add("addon-modules", moduleArray);
                addons.add(addonJson);
            });
            json.set("loaded_addons", addons);

            fetchDebugMessages(values -> json.add("debug_log", values));
            consumer.accept(json);
        });
    }

    private static void fetchDebugMessages(Consumer<JsonArray> consumer) {
        LinkedList<DebugBuilder> debugLog = SimplePets.getDebugLogger().getDebugLog();
        JsonArray array = new JsonArray();
        while (!debugLog.isEmpty()) {
            JsonObject json = new JsonObject();
            DebugBuilder builder = debugLog.pollFirst();

            Instant instant = Instant.ofEpochMilli(builder.timestamp);
            ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd | HH:mm:ss:SSS");
            String output = formatter.format(zdt);
            json.add("time/date", output);
            json.add("level", builder.getLevel().getName());

            JsonArray messages = new JsonArray();
            builder.getMessages().forEach(messages::add);
            json.add("message", messages);
            if (builder.getCaller() != null) json.add("caller", builder.getCaller());
            array.add(json);
        }
        consumer.accept(array);
    }

    private static void fetchJenkinsInfo(boolean skipJenkins, Consumer<JsonObject> consumer) {
        if (skipJenkins) {
            consumer.accept(new JsonObject());
            return;
        }

        Properties prop = new Properties();
        try {
            prop.load(PetCore.getInstance().getClass().getResourceAsStream("/jenkins.properties"));
        } catch (IOException ignored) {} // If it fails, it means there is no 'jenkins.properties' file
        int build = Integer.parseInt(String.valueOf(prop.getOrDefault("buildnumber", -1)));

        WebConnector.getInputStreamString("https://bsdevelopment.org/api/jenkins/build-number/SimplePets_v5", PetCore.getInstance(), string -> {
            JsonObject jenkins = new JsonObject();
            jenkins.add("repo", "SimplePets_v5");
            jenkins.add("plugin_build_number", build);

            try {
                JsonObject buildResult = (JsonObject) Json.parse(string);

                if (!buildResult.isEmpty()) {
                    if (buildResult.names().contains("build-number")) {
                        int latestBuild = buildResult.getInt("build-number", -1);

                        // New build found
                        if (latestBuild > build) jenkins.add("number_of_builds_behind", (latestBuild - build));

                        // Using a custom build that is ahead of the Jenkins builds
                        if (build > latestBuild) jenkins.add("number_of_builds_behind", "From The Future :O");

                        jenkins.add("jenkins_build_number", latestBuild);
                    } else {
                        jenkins.add("reason", "Missing repo: SimplePets_v5");
                        jenkins.add("parsed_string", string);
                    }
                } else {
                    jenkins.add("reason", "Empty");
                    jenkins.add("parsed_string", string);
                }
            } catch (Exception e) {
                jenkins.add("parsed_string", string);
                jenkins.add("error_parsing_json", e.getMessage());
            }

            consumer.accept(jenkins);
        });
    }

    private static JsonArray fetchPlugins() {
        // Fetches the plugins the server uses (used for finding conflicts)
        JsonArray array = new JsonArray();
        List<String> plugins = new ArrayList<>();
        Arrays.asList(Bukkit.getPluginManager().getPlugins()).forEach(plugin -> {
            if (plugin.isEnabled()) {
                String name = plugin.getDescription().getName();
                String ver = plugin.getDescription().getVersion();
                plugins.add(name + " (" + ver + ")");
            }
        });
        plugins.sort(Comparator.naturalOrder());
        plugins.forEach(array::add);

        return array;
    }

    private static void fetchServerInfo(Consumer<JsonObject> consumer) {
        JsonObject info = new JsonObject();

        PetCore.ServerInformation serverInformation = PetCore.SERVER_INFORMATION;
        info.add("java", serverInformation.getJava());

        info.add("server-information", new JsonObject()
            .add("server-type", serverInformation.getServerType())
            .add("minecraft-version", serverInformation.getMinecraftVersion())
            .add("server-build", serverInformation.getBuildVersion())
            .add("bukkit-version", serverInformation.getBukkitVersion())
            .add("raw-version", serverInformation.getRawVersion())
        );
        info.add("bslib-server-version", new JsonObject()
            .add("nms", ServerVersion.getVersion().getNMS())
            .add("name", ServerVersion.getVersion().name())
        );
        info.add("simplepets", new JsonObject()
                .add("version", PetCore.getInstance().getDescription().getVersion())
                .add("legacy-pathfinding", ConfigOption.INSTANCE.LEGACY_PATHFINDING_ENABLED.getValue())
        );

        consumer.accept(info);
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
        } catch (IOException var7) {
            var7.printStackTrace();
        }
    }

    private static JsonArray toArray(List<String> list) {
        JsonArray array = new JsonArray();
        list.forEach(array::add);
        return array;
    }
}
