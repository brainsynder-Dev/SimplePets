package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.ISizable;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.SLIME)
public interface IEntitySlimePet extends ISizable, IEntityPet {
}
