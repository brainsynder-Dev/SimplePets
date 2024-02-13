package simplepets.brainsynder.commands.list;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.json.Json;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.json.WriterConfig;
import lib.brainsynder.update.UpdateResult;
import lib.brainsynder.utils.AdvString;
import lib.brainsynder.web.WebConnector;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.commands.PetsCommand;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.utils.Premium;

import java.io.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ICommand(
        name = "debug",
        usage = "[skip-jenkins]",
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
        sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+" §7Fetching Debug Information...");
        boolean skipJenkins = false;

        if (args.length > 0) skipJenkins = Boolean.parseBoolean(args[0]);

        fetchDebug(json -> {
            log(getPlugin().getDataFolder(), "debug.json", json.toString(WriterConfig.PRETTY_PRINT));
            sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+" §7Generated §e'plugins/SimplePets/debug.json'");

            WebConnector.uploadPaste(PetCore.getInstance(), json.toString(WriterConfig.PRETTY_PRINT), s -> {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + " §7Uploaded to PasteLog:§e " + s);
            });
        }, skipJenkins);
    }

    private static void fetchConfig (Consumer<String> consumer) {
        File file = new File(PetCore.getInstance().getDataFolder(), "config.yml");
        try {
            LineNumberReader lnr = new LineNumberReader(new FileReader(file));
            String line = "";
            StringBuilder builder = new StringBuilder();
            while (line != null) {
                line = lnr.readLine();
                if (line == null) break;
                if (line.contains("Host:")) {
                    String username = AdvString.after("Host:", line);
                    line = line.replace(username, " HIDDEN");
                }
                if (line.contains("Username:")) {
                    String username = AdvString.after("Username:", line);
                    line = line.replace(username, " HIDDEN");
                }
                if (line.contains("Password:")) {
                    String username = AdvString.after("Password:", line);
                    line = line.replace(username, " HIDDEN");
                }

                builder.append(line).append("\n");
            }

            WebConnector.uploadPaste(PetCore.getInstance(), builder.toString(), consumer::accept);
        }catch (Exception e) {
            consumer.accept("ERROR: "+e.getMessage());
        }
    }

    public static void fetchDebug (Consumer<JsonObject> consumer, boolean skipJenkins) {
        JsonObject json = new JsonObject();
        json.add("premium_purchase", Premium.isPremium());
        json.add("reloaded", PetCore.getInstance().wasReloaded());
        PetCore.getInstance().checkWorldGuard(value -> json.add("worldguard_config_check", value));
        fetchServerInfo(object -> json.add("server", object));
        json.add("plugins", fetchPlugins());

        JsonArray addons = new JsonArray();
        PetCore.getInstance().getAddonManager().getLocalDataMap().forEach((localData, modules) -> {
            JsonObject addonJson = new JsonObject();
            JsonArray moduleArray = new JsonArray();
            modules.forEach(module -> {
                moduleArray.add("Module: '"+module.getNamespace().namespace()+"' | Loaded: "+module.isLoaded()+" | Enabled: "+module.isEnabled());
            });

            addonJson.add("addon-name", localData.getName() + "(v"+localData.getVersion()+") by: "+localData.getAuthors().toString()
                    .replace("[", "").replace("]", ""));
            addonJson.add("addon-file-name", localData.getFile().getName());
            addonJson.add("addon-modules", moduleArray);
            addons.add(addonJson);
        });
        json.set("loaded_addons", addons);

        if (skipJenkins) {
            fetchDebugMessages(values -> json.add("debug_log", values));
            consumer.accept(json);
            return;
        }
        fetchConfig(conf -> {
            json.set("config", conf);

            fetchJenkinsInfo(object -> {
                json.add("jenkins", object);
                fetchDebugMessages(values -> json.add("debug_log", values));
                consumer.accept(json);
            });
        });
    }

    private static void fetchDebugMessages (Consumer<JsonArray> consumer) {
        LinkedList<DebugBuilder> debugLog = SimplePets.getDebugLogger().getDebugLog();
        JsonArray array = new JsonArray();
        while (!debugLog.isEmpty()) {
            JsonObject json = new JsonObject();
            DebugBuilder builder = debugLog.pollFirst();

            Instant instant = Instant.ofEpochMilli ( builder.timestamp );
            ZonedDateTime zdt = ZonedDateTime.ofInstant ( instant , ZoneOffset.UTC );
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern ( "yyyy/MM/dd | HH:mm:ss:SSS" );
            String output = formatter.format ( zdt );
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

    private static void fetchJenkinsInfo(Consumer<JsonObject> consumer) {
        UpdateResult result = PetCore.getInstance().getUpdateUtils().getResult();
        int build = result.getCurrentBuild();
        WebConnector.getInputStreamString("https://pluginwiki.us/version/jenkins/"+result.getRepo(), PetCore.getInstance(), string -> {
            JsonObject jenkins = new JsonObject();
            jenkins.add("repo", result.getRepo());
            jenkins.add("plugin_build_number", build);

            try {
                JsonObject buildResult = (JsonObject) Json.parse(string);

                if (!buildResult.isEmpty()) {
                    if (buildResult.names().contains("build")) {
                        int latestBuild = buildResult.getInt("build", -1);

                        // New build found
                        if (latestBuild > build) jenkins.add("number_of_builds_behind", (latestBuild - build));

                        // Using a custom build that is ahead of the Jenkins builds
                        if (build > latestBuild) jenkins.add("number_of_builds_behind", "From The Future :O");

                        jenkins.add("jenkins_build_number", latestBuild);
                    }else{
                        jenkins.add("reason", "Missing repo: "+result.getRepo());
                        jenkins.add("parsed_string", string);
                    }
                }else{
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

    private static JsonArray fetchPlugins () {
        // Fetches the plugins the server uses (used for finding conflicts)
        JsonArray array = new JsonArray();
        List<String> plugins = new ArrayList<>();
        Arrays.asList(Bukkit.getPluginManager().getPlugins()).forEach(plugin -> {
            if (plugin.isEnabled()) {
                String name = plugin.getDescription().getName();
                String ver = plugin.getDescription().getVersion();
                plugins.add(name+" ("+ver+")");
            }
        });
        plugins.sort(Comparator.naturalOrder());
        plugins.forEach(array::add);

        return array;
    }

    private static void fetchServerInfo(Consumer<JsonObject> consumer) {
        JsonObject info = new JsonObject();

        String java = System.getProperty("java.version");
        int pos = java.indexOf('.');
        pos = java.indexOf('.', pos + 1);
        if (pos != -1) {
            info.add("java", Double.parseDouble(java.substring(0, pos).replace(".0", "")));
        } else {
            info.add("java", java);
        }

        String version = Bukkit.getVersion();
        info.add("raw", version);

        Pattern pattern = Pattern.compile("git-(\\w+)-\"(\\w+)\"");
        Matcher matcher = pattern.matcher(version);
        if (matcher.find()) {
            info.add("server_type", matcher.group(1));
        } else {
            // Backup just in case it does not catch the pattern
            if (version.contains("-")) {
                String[] args = version.split("-");
                if (args.length >= 2) {
                    String type = args[1];
                    if (type.toLowerCase().contains("spigot") || type.toLowerCase().contains("bukkit")) {
                        info.add("server_type", type);
                    }
                }
            }
            if (!info.names().contains("server_type")) info.add("server_type", version);
        }

        info.add("bukkit_version", Bukkit.getBukkitVersion());
        info.add("server-version", new JsonObject()
                .add("nms", ServerVersion.getVersion().getNMS())
                .add("name", ServerVersion.getVersion().name())
        );
        info.add("simplepets", PetCore.getInstance().getDescription().getVersion());

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

    private static JsonArray toArray (List<String> list) {
        JsonArray array = new JsonArray();
        list.forEach(array::add);
        return array;
    }
}
