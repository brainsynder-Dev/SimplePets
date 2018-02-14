package simplepets.brainsynder.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.links.IPlotSquaredLink;
import simplepets.brainsynder.links.IWorldGuardLink;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.nms.entities.type.main.IImpossaPet;
import simplepets.brainsynder.pet.PetMoveEvent;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.ReflectionUtil;
import simplepets.brainsynder.utils.LinkRetriever;

public class OnPetSpawn extends ReflectionUtil implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Entity e = event.getEntity();
        if (ReflectionUtil.getEntityHandle(e) instanceof IImpossaPet) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawnUnBlock(CreatureSpawnEvent event) {
        Entity e = event.getEntity();
        if (ReflectionUtil.getEntityHandle(e) instanceof IImpossaPet && event.isCancelled()) {
            if (PetCore.get().getConfiguration().getBoolean("Complete-Mobspawning-Deny-Bypass")) {
                event.setCancelled(false);
                return;
            }

            if (LinkRetriever.getProtectionLink(IWorldGuardLink.class).allowPetSpawn(event.getLocation())) {
                event.setCancelled(false);
                return;
            }

            if (LinkRetriever.getProtectionLink(IPlotSquaredLink.class).allowPetSpawn(event.getLocation())) {
                event.setCancelled(false);
            }
        }
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCreatureSpawn(EntitySpawnEvent event) {
        Entity e = event.getEntity();
        if (ReflectionUtil.getEntityHandle(e) instanceof IImpossaPet) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawnHigh(EntitySpawnEvent event) {
        Entity e = event.getEntity();
        if (ReflectionUtil.getEntityHandle(e) instanceof IImpossaPet && event.isCancelled()) {
            if (PetCore.get().getConfiguration().getBoolean("Complete-Mobspawning-Deny-Bypass")) {
                event.setCancelled(false);
                return;
            }

            if (LinkRetriever.getProtectionLink(IWorldGuardLink.class).allowPetSpawn(event.getLocation())) {
                event.setCancelled(false);
                return;
            }

            if (LinkRetriever.getProtectionLink(IPlotSquaredLink.class).allowPetSpawn(event.getLocation())) {
                event.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onMove(PetMoveEvent e) {
        try {
            if (e.getEntity() == null) return;
            if (e.getEntity().getPet() == null) return;
            if (e.getEntity().getOwner() == null) return;
            IEntityPet entity = e.getEntity();
            PetOwner petOwner = PetOwner.getPetOwner(entity.getOwner());
            if (e.getCause() == PetMoveEvent.Cause.RIDE) {
                if (!LinkRetriever.canRidePet(petOwner, entity.getEntity().getLocation())) {
                    petOwner.removePet();
                    entity.getOwner().sendMessage(PetCore.get().getMessages().getString("Pet-No-Enter", true));
                }
                return;
            }

            if (!LinkRetriever.canPetEnter(petOwner, entity.getEntity().getLocation())) {
                petOwner.removePet();
                entity.getOwner().sendMessage(PetCore.get().getMessages().getString("Pet-No-Enter", true));
            }
        } catch (Exception ignored) {
        }
    }
}
