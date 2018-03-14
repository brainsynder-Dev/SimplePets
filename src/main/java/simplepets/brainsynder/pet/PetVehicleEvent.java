package simplepets.brainsynder.pet;

import lombok.Getter;
import simplepets.brainsynder.event.CancellablePetEvent;

public class PetVehicleEvent extends CancellablePetEvent {
    @Getter
    private Pet pet;
    @Getter
    private Type eventType;

    public PetVehicleEvent(Pet pet, Type type) {
        super(PetEventType.VEHICLE);
        this.pet = pet;
        eventType = type;
    }

    public enum Type {
        DISMOUNT,
        MOUNT
    }
}
