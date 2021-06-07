package simplepets.brainsynder.api.event.entity;

import org.bukkit.Location;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.SimplePetEvent;

public class PetMoveEvent extends SimplePetEvent {
    private final IEntityPet entity;
    private final Location targetLocation;
    private final Cause cause;

    public PetMoveEvent(IEntityPet entity, Cause cause) {
        this.entity = entity;
        if (cause == Cause.RIDE) {
            this.targetLocation = entity.getEntity().getLocation();
        } else {
            this.targetLocation = entity.getPetUser().getUserLocation().get();
        }
        this.cause = cause;
    }


    public IEntityPet getEntity() {
        return entity;
    }

    public Location getTargetLocation() {
        return targetLocation;
    }

    public Cause getCause() {
        return cause;
    }


    public enum Cause {
        RIDE,
        WALK
    }
}
