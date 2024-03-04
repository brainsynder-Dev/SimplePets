package simplepets.brainsynder.listeners;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.nms.Tellraw;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import simplepets.brainsynder.PetCore;

import java.util.concurrent.TimeUnit;

/**
 * This listener is only active when the servers version is not supported
 */
public class BrokenVersionListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.isOp()) return;
        PetCore.getInstance().getScheduler().getImpl().runAtEntityLater(player, () -> {
            String version = ServerVersion.getVersion().name().replace("v", "").replace("_", ".");
            player.sendMessage("§4[§cSimplePets§4] §7SimplePets has encountered an error, " +
                    "We seems to be missing support for your version §4(§c"+ version +"§4)");
            Tellraw.fromLegacy("&4[&cSimplePets&4] &7Please download the version for your server from the ")
                    .then("JENKINS (Click Me)").color(ChatColor.RED).link("https://ci.pluginwiki.us/job/SimplePets_v5/")
                    .send(player);
            player.sendMessage("§4[§cSimplePets§4] §7Check if there is a §cSimplePets-"+version+".jar §7download (IF AVAILABLE)");
        }, 1L, TimeUnit.SECONDS);
    }
}
