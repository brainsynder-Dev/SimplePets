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
public class OnJoin implements Listener {
    @EventHandler
    public void onKick(PlayerKickEvent e) {
        Player player = e.getPlayer();
        PetOwner owner = PetOwner.getPetOwner(player);
        if (owner.hasPetToRespawn()) owner.setPetToRespawn(null);
        owner.getFile().save();
        if (owner.hasPet()) owner.removePet();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        try {
            PetOwner owner = PetOwner.getPetOwner(player);
            owner.reloadData();
            if (!owner.hasPetToRespawn()) return;
            if (PetCore.get().getConfiguration().getBoolean("Respawn-Last-Pet-On-Login")) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        owner.respawnPet();
                    }
                }.runTaskLater(PetCore.get(), 10);
            }
        } catch (Exception ignored) {
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        PetOwner owner = PetOwner.getPetOwner(player);
        if (owner.hasPetToRespawn()) owner.setPetToRespawn(null);
        owner.getFile().save();
        if (owner.hasPet()) owner.removePet();
    }

    @EventHandler
    public void onPortal(EntityPortalEnterEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            PetOwner owner = PetOwner.getPetOwner(player);
            if (owner.hasPet()) owner.removePet();
        } else {
            if (ReflectionUtil.getEntityHandle(e.getEntity()) instanceof IEntityPet) {
                IEntityPet pet = (IEntityPet) ReflectionUtil.getEntityHandle(e.getEntity());
                PetOwner owner = PetOwner.getPetOwner(pet.getOwner());
                if (owner.hasPet()) {
                    owner.removePet();
                    return;
                }
            }
        }
    }
}
