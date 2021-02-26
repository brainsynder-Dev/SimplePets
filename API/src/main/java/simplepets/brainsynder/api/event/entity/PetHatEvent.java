package simplepets.brainsynder.api.event.entity;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.CancellablePetEvent;

public class PetHatEvent extends CancellablePetEvent {
    private final IEntityPet entityPet;
    private final Type eventType;

    public PetHatEvent(IEntityPet pet, Type type) {
        this.entityPet = pet;
        eventType = type;
    }

    public IEntityPet getEntityPet() {return this.entityPet;}

    public Type getEventType() {return this.eventType;}

    public enum Type {
        SET,
        REMOVE
    }
}