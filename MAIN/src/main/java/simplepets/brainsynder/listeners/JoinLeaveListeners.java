package simplepets.brainsynder.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.impl.PetOwner;
import simplepets.brainsynder.sql.PlayerSQL;

public class JoinLeaveListeners implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        SimplePets.getUserManager().getPetUser(event.getPlayer()).ifPresent(user -> ((PetOwner) user).markForRespawn());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        SimplePets.getUserManager().getPetUser(event.getPlayer()).ifPresent(user -> ((PetOwner) user).markForRespawn());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerSQL.getInstance().fetchData(event.getPlayer().getUniqueId(), data -> {
                    SimplePets.getUserManager().getPetUser(event.getPlayer()).ifPresent(user -> {
                        ((PetOwner) user).loadCompound(data);
                    });
                });
            }
        }.runTaskLater(PetCore.getInstance(), 10);
    }
}
