package simplepets.brainsynder.api.event.pet;

import org.bukkit.Location;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.SimplePetEvent;

public class PetMoveEvent extends SimplePetEvent {
    private IEntityPet entity;
    private Location targetLocation;
    private Cause cause;

    public PetMoveEvent(IEntityPet entity, Cause cause, boolean async) {
        super(PetEventType.MOVE, async);
        this.entity = entity;
        this.cause = cause;
        if (cause == Cause.RIDE) {
            this.targetLocation = entity.getEntity().getLocation();
        } else {
            this.targetLocation = entity.getOwner().getLocation();
        }
    }

    public PetMoveEvent(IEntityPet entity, Cause cause) {
        super(PetEventType.MOVE);
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
        WALK,
        FOLLOW
    }
}
