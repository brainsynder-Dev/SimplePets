package simplepets.brainsynder.pet;

import lombok.Getter;
import simplepets.brainsynder.event.CancellablePetEvent;

public class PetHatEvent extends CancellablePetEvent {
    @Getter
    private Pet pet;
    @Getter
    private Type eventType;

    public PetHatEvent(Pet pet, Type type) {
        super(PetEventType.HAT);
        this.pet = pet;
        eventType = type;
    }

    public enum Type {
        SET,
        REMOVE
    }
}
