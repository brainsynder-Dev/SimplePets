package simplepets.brainsynder.api.event.pet;

import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.pet.Pet;

public class PetRemoveEvent extends CancellablePetEvent {
    private Pet pet;

    public PetRemoveEvent(Pet pet) {
        super(PetEventType.REMOVE);
        this.pet = pet;
    }

    public Pet getPet() {return this.pet;}
}
