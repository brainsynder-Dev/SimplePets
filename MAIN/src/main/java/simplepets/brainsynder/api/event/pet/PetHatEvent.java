package simplepets.brainsynder.api.event.pet;

import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.pet.Pet;

public class PetHatEvent extends CancellablePetEvent {
    private final Pet pet;
    private final Type eventType;

    public PetHatEvent(Pet pet, Type type) {
        this.pet = pet;
        eventType = type;
    }

    public Pet getPet() {return this.pet;}

    public Type getEventType() {return this.eventType;}

    public enum Type {
        SET,
        REMOVE
    }
}
