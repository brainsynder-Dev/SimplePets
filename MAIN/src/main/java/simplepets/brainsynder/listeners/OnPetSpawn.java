package simplepets.brainsynder.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IImpossaPet;
import simplepets.brainsynder.api.event.pet.PetMoveEvent;
import simplepets.brainsynder.links.IPlotSquaredLink;
import simplepets.brainsynder.links.IWorldBorderLink;
import simplepets.brainsynder.links.IWorldGuardLink;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.ReflectionUtil;

public class OnPetSpawn extends ReflectionUtil implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Entity e = event.getEntity();
        event.setCancelled(PetCore.getHandle(e) instanceof IImpossaPet);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawnUnBlock(CreatureSpawnEvent event) {
        Entity e = event.getEntity();
        PetCore core = PetCore.get();
        if (event.isCancelled() && core.getSpawner().getHandle(e) instanceof IImpossaPet) {
            if (core.getConfiguration().getBoolean("Complete-Mobspawning-Deny-Bypass")
                    || core.getLinkRetriever().getProtectionLink(IWorldGuardLink.class).allowPetSpawn(event.getLocation())
                    || core.getLinkRetriever().getProtectionLink(IPlotSquaredLink.class).allowPetSpawn(event.getLocation())
                    || core.getLinkRetriever().getProtectionLink(IWorldBorderLink.class).allowPetSpawn(event.getLocation())) {
                event.setCancelled(false);

            }
        }
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCreatureSpawn(EntitySpawnEvent event) {
        Entity e = event.getEntity();
        event.setCancelled(PetCore.get().getSpawner().getHandle(e) instanceof IImpossaPet);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawnUnBlock(EntitySpawnEvent event) {
        Entity e = event.getEntity();
        PetCore core = PetCore.get();
        if (event.isCancelled() && PetCore.getHandle(e) instanceof IImpossaPet) {
            if (core.getConfiguration().getBoolean("Complete-Mobspawning-Deny-Bypass")
                    || core.getLinkRetriever().getProtectionLink(IWorldGuardLink.class).allowPetSpawn(event.getLocation())
                    || core.getLinkRetriever().getProtectionLink(IPlotSquaredLink.class).allowPetSpawn(event.getLocation())
                    || core.getLinkRetriever().getProtectionLink(IWorldBorderLink.class).allowPetSpawn(event.getLocation())) {
                event.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onMove(PetMoveEvent e) {
        try {
            if (e.getEntity() == null) return;
            if (e.getEntity().getPet() != null && e.getEntity().getOwner() != null) {
                IEntityPet entity = e.getEntity();
                PetOwner petOwner = PetOwner.getPetOwner(entity.getOwner());
                PetCore core = PetCore.get();
                if (e.getCause() == PetMoveEvent.Cause.RIDE) {
                    if (!core.getLinkRetriever().canRidePet(petOwner, entity.getEntity().getLocation())) {
                        petOwner.getPet().setVehicle(false, false);
                        entity.getOwner().sendMessage(PetCore.get().getMessages().getString("Pet-No-Enter", true));
                    }
                    return;
                }

                if (!core.getLinkRetriever().canPetEnter(petOwner, entity.getEntity().getLocation())) {
                    petOwner.removePet();
                    entity.getOwner().sendMessage(PetCore.get().getMessages().getString("Pet-No-Enter", true));
                }
            }
        } catch (Exception ignored) {
        }
    }
}
