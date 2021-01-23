package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IEntityFishPet;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.SALMON)
public interface IEntitySalmonPet extends IEntityFishPet {
}
