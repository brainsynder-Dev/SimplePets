package simplepets.brainsynder.listeners;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;

import java.util.concurrent.TimeUnit;

public class LocationChangeListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) return;
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN) return;
        if (event.getTo() == null) return;

        World fromWorld = event.getFrom().getWorld();
        World toWorld = event.getTo().getWorld();

        if (fromWorld != null && toWorld != null && !fromWorld.equals(toWorld)) {
            if (!ConfigOption.INSTANCE.REMOVE_PET_ON_WORLD_CHANGE.getValue()) return;
            Player player = event.getPlayer();
            SimplePets.getUserManager().getPetUser(player).ifPresent(user -> {
                user.cacheAndRemove();
                PetCore.getInstance().getScheduler().getImpl().runAtEntityLater(player, user::summonCachedPets, 2L, TimeUnit.SECONDS);
            });
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        if (!ConfigOption.INSTANCE.REMOVE_PET_ON_WORLD_CHANGE.getValue()) return;

        Player player = event.getPlayer();
        SimplePets.getUserManager().getPetUser(player).ifPresent(user -> {
            if (user.hasPets()) {
                user.cacheAndRemove();
                PetCore.getInstance().getScheduler().getImpl().runAtEntityLater(player, user::summonCachedPets, 2L, TimeUnit.SECONDS);
            }
        });
    }

}
