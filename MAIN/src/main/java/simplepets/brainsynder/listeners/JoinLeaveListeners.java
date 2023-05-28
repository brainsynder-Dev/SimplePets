package simplepets.brainsynder.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.impl.PetOwner;

public class JoinLeaveListeners implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        SimplePets.getUserManager().getPetUser(event.getPlayer()).ifPresent(user -> {
            if (((PetOwner) user).isLoaded()) ((PetOwner) user).markForRespawn();
        });
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        SimplePets.getUserManager().getPetUser(event.getPlayer()).ifPresent(user -> {
            if (((PetOwner) user).isLoaded()) ((PetOwner) user).markForRespawn();
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PetCore.getInstance().getSqlHandler().fetchData(event.getPlayer().getUniqueId()).whenComplete((data, throwable) -> {
            SimplePets.getUserManager().getPetUser(event.getPlayer()).ifPresent(user -> {
                ((PetOwner) user).loadCompound(data);
            });
        });
    }
}
