package simplepets.brainsynder.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.ReflectionUtil;

@SuppressWarnings("ALL")
public class OnJoin implements Listener {
    @EventHandler
    public void onKick(PlayerKickEvent e) {
        petRemoveOnLeave(e);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (PetCore.get().getConfiguration().getBoolean("Check-For-New-Builds", true) && (PetCore.get().getLatestBuild() != -1)) {
            if (player.hasPermission("pet.update")) {
                player.sendMessage("§6[SimplePets Update] §7There is a new build available on the jenkins");
                player.sendMessage("§6[SimplePets Update]§7 http://ci.pluginwiki.us/job/" + PetCore.get().getJenkinsProperties().getProperty("repo") + "/" + PetCore.get().getLatestBuild() + "/");
            }
        }

        if (!PetCore.get().getConfiguration().getBoolean("Respawn-Last-Pet-On-Login")) return;
        try {
            PetOwner owner = PetOwner.getPetOwner(player);
            if (owner == null) return;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!owner.hasPetToRespawn()) return;
                    owner.respawnPet();
                }
            }.runTaskLater(PetCore.get(), 30);
        } catch (Exception ignored) {

        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        petRemoveOnLeave(e);
    }

    @EventHandler
    public void onPortal(EntityPortalEnterEvent e) {
        if (e.getEntity() instanceof Player) {
            PetOwner owner = PetOwner.getPetOwner((Player) e.getEntity());
            if (owner == null) return;
            if (owner.hasPet()) owner.removePet();
        } else {
            if (ReflectionUtil.getEntityHandle(e.getEntity()) instanceof IEntityPet) {
                IEntityPet pet = (IEntityPet) ReflectionUtil.getEntityHandle(e.getEntity());
                PetOwner owner = PetOwner.getPetOwner(pet.getOwner());
                if (owner == null) return;
                if (owner.hasPet()) {
                    owner.removePet();
                }
            }
        }
    }

    private void petRemoveOnLeave(PlayerEvent e) {
        Player player = e.getPlayer();
        PetOwner owner = PetOwner.getPetOwner(player);
        if (owner == null) return;
        if (owner.hasPetToRespawn()) owner.setPetToRespawn(null);
        owner.getFile().save();
        if (owner.hasPet()) owner.removePet();
        PetOwner.removePlayer(player.getUniqueId());
    }
}
