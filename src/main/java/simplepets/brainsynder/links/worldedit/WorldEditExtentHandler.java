package simplepets.brainsynder.links.worldedit;

import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.logging.AbstractLoggingExtent;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.player.PetOwner;

/**
 * This is a little hacky type fix for the issue with WorldEdit and Duplicating pets
 *
 * This code is from "http://wiki.sk89q.com/wiki/WorldEdit/API/Hooking_EditSession"
 * I modified it a little bit
 *
 */
public class WorldEditExtentHandler {

    @Subscribe
    public void wrapForLogging(EditSessionEvent event) {
        Actor actor = event.getActor();
        if (actor != null && actor.isPlayer()) {
            event.setExtent(new PetExtent(actor, event.getExtent()));
        }
    }
}

class PetExtent extends AbstractLoggingExtent {
    PetExtent(Actor actor, Extent extent) {
        super(extent);
        PetOwner owner = PetOwner.getPetOwner(actor.getUniqueId());

        if (owner == null) return;
        if (!owner.hasPet()) return;

        IPet pet = owner.getPet();
        for (org.bukkit.entity.Entity entity : pet.getEntity().getEntities()) {
            Location loc = entity.getLocation();
            for (Entity we : extent.getEntities()) {
                if (we.getLocation().getX() == loc.getX()) {
                    if (we.getLocation().getY() == loc.getY()) {
                        if (we.getLocation().getZ() == loc.getZ()) {
                            owner.removePet();
                            break;
                        }
                    }
                }
            }
        }
    }
}