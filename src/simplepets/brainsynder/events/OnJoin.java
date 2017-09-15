package simplepets.brainsynder.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.ReflectionUtil;

@SuppressWarnings("ALL")
public class OnJoin extends EventCore implements Listener {
    @EventHandler
    public void onKick(PlayerKickEvent e) {
        Player player = e.getPlayer();
        PetOwner petOwner = PetOwner.getPetOwner(player);
        if (petTypeMap.containsKey(player.getUniqueId()))
            petTypeMap.remove(player.getUniqueId());
        petOwner.getFile().save();
        if (petOwner.hasPet()) {
            petOwner.removePet();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PetOwner petOwner = PetOwner.getPetOwner(player);
        petOwner.reloadData();
        if (PetCore.get().getConfiguration().getBoolean("Respawn-Last-Pet-On-Login")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    petOwner.getFile().respawnPet();
                }
            }.runTaskLater(PetCore.get(), 10);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PetOwner petOwner = PetOwner.getPetOwner(player);
        if (petTypeMap.containsKey(player.getUniqueId()))
            petTypeMap.remove(player.getUniqueId());
        petOwner.getFile().save();
        if (petOwner.hasPet()) {
            petOwner.removePet();
        }
    }

    @EventHandler
    public void onPortal(EntityPortalEnterEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            PetOwner petOwner = PetOwner.getPetOwner(player);
            if (petOwner.hasPet()) {
                petOwner.removePet();
            }
        } else {
            if (ReflectionUtil.getEntityHandle(e.getEntity()) instanceof IEntityPet) {
                IEntityPet pet = (IEntityPet) ReflectionUtil.getEntityHandle(e.getEntity());
                PetOwner petOwner = PetOwner.getPetOwner(pet.getOwner());
                if (petOwner.hasPet()) {
                    petOwner.removePet();
                    return;
                }
            }
        }
    }
}
