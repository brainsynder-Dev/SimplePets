package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IEntityFishPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.wrappers.PufferState;

@EntityPetType(petType = PetType.PUFFERFISH)
public interface IEntityPufferFishPet extends IEntityFishPet {
    PufferState getPuffState();

    void setPuffState(PufferState state);
}
