package simplepets.brainsynder.commands.sub;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.files.JsonFile;
import lib.brainsynder.json.Json;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.update.UpdateResult;
import lib.brainsynder.web.WebConnector;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.commands.annotations.Permission;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@ICommand(
        name = "debug",
        usage = "&r &r &6[] &7/pet debug",
        description = "Collects Information about your server that we use for finding bugs"
)
@Permission(permission = "debug")
public class Debug_SubCommand extends PetSubCommand {
    @Override
    public void run(CommandSender sender) {
        // Collects & formats the servers Java version
        sender.sendMessage("§eFetching Debug Information...");
        JsonObject json = new JsonObject();
        JsonObject info = new JsonObject();

        // Fetches the Java version of the server
        String version = System.getProperty("java.version");
        int pos = version.indexOf('.');
        pos = version.indexOf('.', pos + 1);
        info.add("java", version.substring(0, pos));

        String bkV = Bukkit.getVersion();
        info.add("reloaded", PetCore.get().wasReloaded());
        info.add("raw_version", bkV);
        info.add("bukkit_version", Bukkit.getBukkitVersion());
        info.add("nms_version", ServerVersion.getVersion().name());
        info.add("simplepets", PetCore.get().getDescription().getVersion());

        UpdateResult result = PetCore.get().getUpdateUtils().getResult();
        int build = result.getCurrentBuild();
        WebConnector.getInputStreamString("http://pluginwiki.us/version/builds.json", PetCore.get(), string -> {
            JsonObject jenkins = new JsonObject();
            jenkins.add("repo", result.getRepo());
            jenkins.add("plugin_build_number", build);

            try {
                JsonObject main = (JsonObject) Json.parse(string);
                if (!main.isEmpty()) {
                    if (main.names().contains(result.getRepo())) {
                        int latestBuild = main.getInt(result.getRepo(), -1);

                        // New build found
                        if (latestBuild > build) jenkins.add("number_of_builds_behind", (latestBuild - build));

                        jenkins.add("jenkins_build_number", latestBuild);
                    }
                }
            }catch (Exception e) {
                jenkins.add("error_parsing_json", e.getMessage());
            }
            info.add("jenkins", jenkins);

            json.add("info", info);

            if (bkV.toLowerCase().contains("paper")) {
                json.add("type", "PaperSpigot");
            }else if (bkV.toLowerCase().contains("taco")) {
                json.add("type", "TacoSpigot");
            }else if (bkV.toLowerCase().contains("spigot")) {
                json.add("type", "Spigot");
            }else{
                json.add("type", "CraftBukkit/Bukkit");
            }

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
            json.add("plugins", array);

            sender.sendMessage("§cSaved Debug data to 'Debug.json' (in the SimplePets folder)...");
            File file = new File(PetCore.get().getDataFolder(), "Debug.json");
            if (file.exists()) file.delete();
            JsonFile jsonFile = new JsonFile(file, true);
            json.forEach(member -> jsonFile.set(member.getName(), member.getValue()));
            jsonFile.save();
        });
    }


}
