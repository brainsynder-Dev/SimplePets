package simplepets.brainsynder.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;

public class JoinLeaveListeners implements Listener {

    @EventHandler
    public void onLeave (PlayerQuitEvent event) {
        SimplePets.getUserManager().getPetUser(event.getPlayer()).ifPresent(PetUser::removePets);
    }

    @EventHandler
    public void onKick (PlayerKickEvent event) {
        SimplePets.getUserManager().getPetUser(event.getPlayer()).ifPresent(PetUser::removePets);
    }

}
