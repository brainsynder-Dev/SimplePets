package simplepets.brainsynder.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import simplepets.brainsynder.api.plugin.SimplePets;

public class PetSpawnListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Entity e = event.getEntity();
        if (!SimplePets.isPetEntity(e)) return;
        event.setCancelled(false);
    }
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCreatureSpawn(EntitySpawnEvent event) {
        Entity e = event.getEntity();
        if (!SimplePets.isPetEntity(e)) return;
        event.setCancelled(false);
    }
}
