package simplepets.brainsynder.pet;

import lombok.Getter;
import org.bukkit.Location;
import simplepets.brainsynder.event.SimplePetEvent;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;

public class PetMoveEvent extends SimplePetEvent {
    @Getter
    private IEntityPet entity;
    @Getter
    private Location targetLocation;
    @Getter
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

    public enum Cause {
        RIDE,
        WALK,
        FOLLOW
    }
}
