package simplepets.brainsynder.api.event.user;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.api.user.PetUser;

public class PetNameChangeEvent extends CancellablePetEvent {
    private final PetUser user;
    private final IEntityPet entity;
    private final String name;

    /**
     * This event gets called when the player changes their pets name
     *
     * @param user - The user changing the name
     * @param entity - The entity that is getting targeted
     * @param name - The new name of the pet
     */
    public PetNameChangeEvent(PetUser user, IEntityPet entity, String name) {
        this.user = user;
        this.entity = entity;
        this.name = name;
    }


    public PetUser getUser() {
        return user;
    }

    public IEntityPet getEntity() {
        return entity;
    }

    public String getName() {
        return name;
    }
}
