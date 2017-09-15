package simplepets.brainsynder.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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
    public void onCreatureSpawnUnBlock(EntitySpawnEvent event) {
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
    
    /*@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSpawn(EntitySpawnEvent event) {
        Object handle = ReflectionUtil.getEntityHandle(event.getEntity());
        if (event.isCancelled()) {
            if (handle instanceof IImpossaPet) {
                if (PetCore.get().getConfiguration().getBoolean("Complete-Mobspawning-Deny-Bypass")) {
                    event.setCancelled(false);
                    return;
                }
                
                if (LinkRetriever.getProtectionLink(IWorldGuardLink.class).allowPetSpawn(event.getLocation())) {
                    event.setCancelled(false);
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSpawn(CreatureSpawnEvent event) {
        Object handle = ReflectionUtil.getEntityHandle(event.getEntity());
        if (event.isCancelled()) {
            if (handle instanceof IImpossaPet) {
                if (PetCore.get().getConfiguration().getBoolean("Complete-Mobspawning-Deny-Bypass")) {
                    event.setCancelled(false);
                    return;
                }
                if (LinkRetriever.getProtectionLink(IWorldGuardLink.class).allowPetSpawn(event.getLocation())) {
                    event.setCancelled(false);
                }
            }
        }
    }*/

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        try {
            Player player = e.getPlayer();
            PetOwner petOwner = PetOwner.getPetOwner(player);
            if (petOwner.hasPet()) {
                if (!LinkRetriever.getProtectionLink(IWorldGuardLink.class).allowPetEntry(player.getLocation())) {
                    petOwner.removePet();
                    player.sendMessage(PetCore.get().getMessages().getString("Pet-No-Enter", true));
                    return;
                }
                if (!LinkRetriever.getProtectionLink(IPlotSquaredLink.class).allowPetEntry(player.getLocation())) {
                    petOwner.removePet();
                    player.sendMessage(PetCore.get().getMessages().getString("Pet-No-Enter", true));
                }
            }
        } catch (Exception ignored) {
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
            if (!LinkRetriever.getProtectionLink(IWorldGuardLink.class).allowPetEntry(e.getTargetLocation())) {
                petOwner.removePet();
                entity.getOwner().sendMessage(PetCore.get().getMessages().getString("Pet-No-Enter", true));
                return;
            }
            if (!LinkRetriever.getProtectionLink(IPlotSquaredLink.class).allowPetEntry(e.getTargetLocation())) {
                petOwner.removePet();
                entity.getOwner().sendMessage(PetCore.get().getMessages().getString("Pet-No-Enter", true));
            }
        } catch (Exception ignored) {
        }
    }
}
