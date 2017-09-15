package simplepets.brainsynder.pet;

import lombok.Getter;
import simplepets.brainsynder.event.CancellablePetEvent;

public class PetRemoveEvent extends CancellablePetEvent {
    @Getter
    private Pet pet;

    public PetRemoveEvent(Pet pet) {
        super(PetEventType.REMOVE);
        this.pet = pet;
    }
}
