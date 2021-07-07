package simplepets.brainsynder.api.event.entity.movment;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.entity.PetMoveEvent;

/**
 * This event gets called when ever a pet that is mounted jumps
 */
public class PetJumpEvent extends PetMoveEvent {
    private double jumpHeight;

    public PetJumpEvent(IEntityPet entity, double jumpHeight) {
        super(entity);
        this.jumpHeight = jumpHeight;
    }

    public double getJumpHeight() {
        return jumpHeight;
    }

    public void setJumpHeight(double jumpHeight) {
        this.jumpHeight = jumpHeight;
    }
}
