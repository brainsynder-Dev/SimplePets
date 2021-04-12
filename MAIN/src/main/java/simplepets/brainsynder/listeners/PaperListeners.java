package simplepets.brainsynder.listeners;

import io.papermc.paper.event.player.PlayerNameEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.misc.IImpossaPet;

public class PaperListeners implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onNameTagUsed(PlayerNameEntityEvent event) {
        Object handle = PetCore.getHandle(event.getEntity());
        if (handle instanceof IImpossaPet) event.setCancelled(true);
    }
}
