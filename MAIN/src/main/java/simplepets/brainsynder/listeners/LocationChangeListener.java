package simplepets.brainsynder.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;

public class LocationChangeListener implements Listener {

    @EventHandler
    public void onWorldChange (PlayerChangedWorldEvent event) {
        if (!ConfigOption.INSTANCE.REMOVE_PET_ON_WORLD_CHANGE.getValue()) return;

        SimplePets.getUserManager().getPetUser(event.getPlayer()).ifPresent(user -> {
            user.cacheAndRemove();
            Bukkit.getScheduler().runTaskLater(PetCore.getInstance(), user::summonCachedPets, 40);
        });
    }

    @EventHandler
    public void onTeleport (PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN) return;

        SimplePets.getUserManager().getPetUser(event.getPlayer()).ifPresent(user -> {
            user.cacheAndRemove();
            Bukkit.getScheduler().runTaskLater(PetCore.getInstance(), user::summonCachedPets, 40);
        });
    }

}
