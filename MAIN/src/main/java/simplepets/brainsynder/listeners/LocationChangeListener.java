package simplepets.brainsynder.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;

import java.util.concurrent.TimeUnit;

public class LocationChangeListener implements Listener {

    @EventHandler
    public void onWorldChange (PlayerChangedWorldEvent event) {
        if (!ConfigOption.INSTANCE.REMOVE_PET_ON_WORLD_CHANGE.getValue()) return;

        Player player = event.getPlayer();
        SimplePets.getUserManager().getPetUser(event.getPlayer()).ifPresent(user -> {
            user.cacheAndRemove();
            PetCore.getInstance().getScheduler().getImpl().runAtEntityLater(player, user::summonCachedPets, 2L, TimeUnit.SECONDS);
        });
    }

    @EventHandler
    public void onTeleport (PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN) return;

        Player player = event.getPlayer();
        SimplePets.getUserManager().getPetUser(event.getPlayer()).ifPresent(user -> {
            user.cacheAndRemove();
            PetCore.getInstance().getScheduler().getImpl().runAtEntityLater(player, user::summonCachedPets, 2L, TimeUnit.SECONDS);
        });
    }

}
