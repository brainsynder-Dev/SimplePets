package simplepets.brainsynder.api.event.entity;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.SimplePetEvent;
import simplepets.brainsynder.api.user.PetUser;

/**
 * This even is called when a pet is removed (Not respawned)
 */
public class PetRemoveEvent extends SimplePetEvent {
    private final PetUser user;
    private final IEntityPet entityPet;

    public PetRemoveEvent(PetUser user, IEntityPet pet) {
        this.user = user;
        this.entityPet = pet;
    }

    public IEntityPet getEntityPet() {return this.entityPet;}

    public PetUser getUser() {
        return user;
    }
}