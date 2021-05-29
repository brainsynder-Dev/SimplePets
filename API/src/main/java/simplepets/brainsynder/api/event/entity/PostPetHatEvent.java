package simplepets.brainsynder.api.event.entity;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.SimplePetEvent;
import simplepets.brainsynder.api.user.PetUser;

/**
 * This event gets called AFTER the pet is on the players head
 */
public class PostPetHatEvent extends SimplePetEvent {
    private final PetUser user;
    private final IEntityPet entityPet;
    private final Type eventType;

    public PostPetHatEvent(PetUser user, IEntityPet pet, Type type) {
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