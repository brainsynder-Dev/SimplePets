package simplepets.brainsynder.api.event.pet;

import org.bukkit.Location;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.SimplePetEvent;

public class PetMoveEvent extends SimplePetEvent {
    private final IEntityPet entity;
    private final Location targetLocation;
    private Cause cause;

    public PetMoveEvent(IEntityPet entity, Cause cause, boolean async) {
        this.entity = entity;
        this.cause = cause;
        if (cause == Cause.RIDE) {
            this.targetLocation = entity.getEntity().getLocation();
        } else {
            this.targetLocation = entity.getOwner().getLocation();
        }
    }

    public PetMoveEvent(IEntityPet entity, Cause cause) {
        this.entity = entity;
        if (cause == Cause.RIDE) {
            this.targetLocation = entity.getEntity().getLocation();
        } else {
            this.targetLocation = entity.getOwner().getLocation();
        }
    }

    public IEntityPet getEntity() {return this.entity;}

    public Location getTargetLocation() {return this.targetLocation;}

    public Cause getCause() {return this.cause;}

    public enum Cause {
        RIDE,
        WALK
    }
}
