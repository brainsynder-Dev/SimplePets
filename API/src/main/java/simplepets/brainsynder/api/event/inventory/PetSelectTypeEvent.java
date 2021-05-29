package simplepets.brainsynder.api.event.inventory;

import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

/**
 * This event gets called when a player selects a pet from the main Pet Selector GUI
 */
public class PetSelectTypeEvent extends CancellablePetEvent {

    private final PetType petType;
    private final PetUser user;

    public PetSelectTypeEvent(PetType type, PetUser user) {
        this.petType = type;
        this.user = user;
    }

    public PetType getPetType() {return this.petType;}

    public PetUser getUser() {return this.user;}
}
