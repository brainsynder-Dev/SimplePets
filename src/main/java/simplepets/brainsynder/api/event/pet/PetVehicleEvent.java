package simplepets.brainsynder.api.event.pet;

import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.pet.Pet;

public class PetVehicleEvent extends CancellablePetEvent {
    private Pet pet;
    private Type eventType;

    public PetVehicleEvent(Pet pet, Type type) {
        super(PetEventType.VEHICLE);
        this.pet = pet;
        eventType = type;
    }

    public Pet getPet() {return this.pet;}

    public Type getEventType() {return this.eventType;}

    public enum Type {
        DISMOUNT,
        MOUNT
    }
}
