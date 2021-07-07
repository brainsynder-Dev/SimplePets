package simplepets.brainsynder.api.event.entity;

import org.bukkit.Location;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.CancellablePetEvent;

/**
 * This event is called when ever a pet moves (walks, controlled, and/or jumps)
 */
public class PetMoveEvent extends CancellablePetEvent {
    private final IEntityPet entity;
    private final Location targetLocation;

    public PetMoveEvent(IEntityPet entity) {
        this.entity = entity;
        targetLocation = entity.getEntity().getLocation();
    }

    public IEntityPet getEntity() {
        return entity;
    }

    public Location getTargetLocation() {
        return targetLocation;
    }
}
