package simplepets.brainsynder.listeners;

import org.bsdevelopment.pluginutils.PluginUtilities;
import org.bsdevelopment.pluginutils.chat.TellrawMessage;
import org.bsdevelopment.pluginutils.chat.decoration.NamedTextColor;
import org.bsdevelopment.pluginutils.version.ServerVersion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * This listener is only active when the servers version is not supported
 */
public class BrokenVersionListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.isOp()) return;
        PluginUtilities.getScheduler().runTaskLater(() -> {
            String version = ServerVersion.getVersion().getVersionName().replace("v", "").replace("_", ".");
            player.sendMessage("§4[§cSimplePets§4] §7SimplePets has encountered an error, " +
                "We seems to be missing support for your version §4(§c" + version + "§4)");
            TellrawMessage.of("&4[&cSimplePets&4] &7Please download the version for your server from the ")
                .then("BUILD SERVER (Click Me)").color(NamedTextColor.RED).link("https://builds.bsdevelopment.org/jobs/SimplePets")
                .send(player);
            player.sendMessage("§4[§cSimplePets§4] §7Check if there is a §cSimplePets.jar §7download");
        }, 20);
    }
}
