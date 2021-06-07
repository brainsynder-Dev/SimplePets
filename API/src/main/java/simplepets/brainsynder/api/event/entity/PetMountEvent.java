package simplepets.brainsynder.api.event.entity;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.CancellablePetEvent;

public class PetMountEvent extends CancellablePetEvent {
    private final IEntityPet entity;

    public PetMountEvent(IEntityPet entity) {
        this.entity = entity;
    }

    public IEntityPet getEntity() {
        return entity;
    }
}
