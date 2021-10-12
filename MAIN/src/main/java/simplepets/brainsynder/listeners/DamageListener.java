package simplepets.brainsynder.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.pet.IPetConfig;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;

import java.util.Optional;

public class DamageListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerFall(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;
        Player player = (Player) event.getEntity();
        if (!player.isInsideVehicle()) return;
        Entity entity = player.getVehicle();
        if (entity == null || !SimplePets.isPetEntity(entity)) return;
        Optional<Object> petHandle = SimplePets.getSpawnUtil().getHandle(entity);
        if (!petHandle.isPresent()) return;
        if (!(petHandle.get() instanceof IEntityPet)) return;
        IEntityPet pet = (IEntityPet) petHandle.get();
        Optional<IPetConfig> config = SimplePets.getPetConfigManager().getPetConfig(pet.getPetType());
        if (!config.isPresent()) return;
        if (config.get().canFly(player)) event.setCancelled(true);
    }
}
