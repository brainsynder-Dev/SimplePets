package simplepets.brainsynder.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import simplepets.brainsynder.api.plugin.SimplePets;

public class PortalListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityPortal(EntityPortalEvent event) {
        Entity entity = event.getEntity();
        if (SimplePets.isPetEntity(entity)) {
            event.setCancelled(true);
        }
    }

}

