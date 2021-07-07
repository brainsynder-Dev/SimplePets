package simplepets.brainsynder.api.event.entity.movment;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.entity.PetMoveEvent;

/**
 * This event gets called when ever a pet gets moved due to being a mount
 */
public class PetRideEvent extends PetMoveEvent {
    public PetRideEvent(IEntityPet entity) {
        super(entity);
    }
}
