package simplepets.brainsynder.api.event.entity;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.api.user.PetUser;

/**
 * This event gets called BEFORE the pet is on the players head
 */
public class PrePetHatEvent extends CancellablePetEvent {
    private final PetUser user;
    private final IEntityPet entityPet;
    private final Type eventType;

    public PrePetHatEvent(PetUser user, IEntityPet pet, Type type) {
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