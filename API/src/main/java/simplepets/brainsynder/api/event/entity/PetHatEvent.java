package simplepets.brainsynder.api.event.entity;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.api.user.PetUser;

public class PetHatEvent extends CancellablePetEvent {
    private final PetUser user;
    private final IEntityPet entityPet;
    private final Type eventType;

    public PetHatEvent(PetUser user, IEntityPet pet, Type type) {
        this.user = user;
        this.entityPet = pet;
        eventType = type;
    }

    public IEntityPet getEntityPet() {return this.entityPet;}

    public Type getEventType() {return this.eventType;}

    public PetUser getUser() {
        return user;
    }

    public enum Type {
        SET,
        REMOVE
    }
}