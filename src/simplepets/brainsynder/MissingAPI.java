package simplepets.brainsynder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scheduler.BukkitRunnable;

public class MissingAPI extends BukkitRunnable {
    private Plugin plugin;

    MissingAPI(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() != 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp()) {
                    PluginDescriptionFile pdf = plugin.getDescription();
                    Plugin pl = plugin.getServer().getPluginManager().getPlugin("SimpleAPI");
                    if (pl != null) {
                        player.sendMessage("§b" + pdf.getName() + " §9>> §7Needs to be reloaded, to link into SimpleAPI");
                    } else {
                        player.sendMessage("§c" + pdf.getName() + " §4>> §7Missing dependency ( https://www.spigotmc.org/resources/24671/ ).");
                    }
                }
            }
        }
    }
}
