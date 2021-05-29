package simplepets.brainsynder.api.event.entity;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.api.user.PetUser;

/**
 * This event gets called when the pet is spawned
 */
public class PetEntitySpawnEvent extends CancellablePetEvent {
    private final PetUser user;
    private final IEntityPet entity;

    public PetEntitySpawnEvent(PetUser user, IEntityPet entity) {
        this.user = user;
        this.entity = entity;
    }

    public PetUser getUser() {
        return user;
    }

    public IEntityPet getEntity() {
        return entity;
    }
}
