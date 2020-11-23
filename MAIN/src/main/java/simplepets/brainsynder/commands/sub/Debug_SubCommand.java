package simplepets.brainsynder.commands.sub;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.files.JsonFile;
import lib.brainsynder.json.Json;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.json.WriterConfig;
import lib.brainsynder.update.UpdateResult;
import lib.brainsynder.web.WebConnector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.commands.annotations.Permission;
import simplepets.brainsynder.utils.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
        WebConnector.getInputStreamString("http://pluginwiki.us/version/?repo=" + result.getRepo(), PetCore.get(), string -> {
            JsonObject jenkins = new JsonObject();
            jenkins.add("repo", result.getRepo());
            jenkins.add("plugin_build_number", build);

            JsonObject main = (JsonObject) Json.parse(string);
            if (!main.isEmpty()) {
                if (main.names().contains("build")) {
                    int latestBuild = main.getInt("build", -1);

                    // New build found
                    if (latestBuild > build) jenkins.add("number_of_builds_behind", (latestBuild - build));

                    jenkins.add("jenkins_build_number", latestBuild);
                }
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

            String jsonString = json.toString(WriterConfig.PRETTY_PRINT);
            CompletableFuture.runAsync(() -> {
                String url = Utilities.saveTextToHastebin(jsonString);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (url != null) {
                            sender.sendMessage(ChatColor.GOLD + url);
                        }else{
                            sender.sendMessage("§cFailed to upload data to Hastebin... Outputting data to Debug.json (in the SimplePets folder)...");

                            File file = new File(PetCore.get().getDataFolder(), "Debug.json");
                            if (file.exists()) file.delete();
                            JsonFile jsonFile = new JsonFile(file, true);
                            json.forEach(member -> jsonFile.set(member.getName(), member.getValue()));
                            jsonFile.save();
                        }
                    }
                }.runTask(PetCore.get());
            });
        });
    }


}
