package simplepets.brainsynder.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import simplepets.brainsynder.api.plugin.SimplePets;

public class DamageListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerFall(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;
        Player player = (Player) event.getEntity();
        if (!player.isInsideVehicle()) return;
        Entity entity = player.getVehicle();
        if (entity != null && SimplePets.isPetEntity(entity)) event.setCancelled(true);
    }
}
