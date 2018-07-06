package simplepets.brainsynder.commands.list.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandPermission;
import simplepets.brainsynder.utils.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@CommandName(name = "debug")
@CommandPermission(permission = "debug")
@CommandDescription(description = "Information about your server that we use for finding bugs")
public class CMD_Debug extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
        // Collects & formats the servers Java version
        p.sendMessage("§eFetching Debug Information...");
        JSONObject json = new JSONObject();
        JSONObject info = new JSONObject();

        // Fetches the Java version of the server
        String version = System.getProperty("java.version");
        int pos = version.indexOf('.');
        pos = version.indexOf('.', pos + 1);
        info.put("java", version.substring(0, pos));

        String bkV = Bukkit.getVersion();
        info.put("reloaded", PetCore.get().wasReloaded());
        info.put("raw_version", bkV);
        info.put("bukkit_version", Bukkit.getBukkitVersion());
        info.put("nms_version", ServerVersion.getVersion().name());
        info.put("simpleapi", Bukkit.getServer().getPluginManager().getPlugin("SimpleAPI").getDescription().getVersion());
        info.put("simplepets", PetCore.get().getDescription().getVersion());

        json.put("info", info);

        if (bkV.toLowerCase().contains("paper")) {
            json.put("type", "PaperSpigot");
        }else if (bkV.toLowerCase().contains("taco")) {
            json.put("type", "TacoSpigot");
        }else if (bkV.toLowerCase().contains("spigot")) {
            json.put("type", "Spigot");
        }else{
            json.put("type", "CraftBukkit/Bukkit");
        }

        // Fetches the plugins the server uses (used for finding conflicts)
        JSONArray array = new JSONArray();
        List<String> plugins = new ArrayList<>();
        Arrays.asList(Bukkit.getPluginManager().getPlugins()).forEach(plugin -> {
            if (plugin.isEnabled()) plugins.add(plugin.getDescription().getName());
        });
        plugins.sort(Comparator.naturalOrder());
        array.addAll(plugins);
        json.put("plugins", array);

        String JSON = json.toJSONString();
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        String jsonString = gson.toJson(new JsonParser ().parse(JSON));
        CompletableFuture.runAsync(() -> {
            String url = Utilities.saveTextToHastebin(jsonString);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (url != null) {
                        p.sendMessage(ChatColor.GOLD + url);
                    }else{
                        p.sendMessage("§cFailed to upload data to Hastebin... Outputting data to Console/Logs...");
                        System.out.println(jsonString);
                    }
                }
            }.runTask(PetCore.get());
        });
    }
}
