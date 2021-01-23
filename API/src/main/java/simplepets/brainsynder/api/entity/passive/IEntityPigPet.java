package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.ISaddle;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.PIG)
public interface IEntityPigPet extends IAgeablePet, ISaddle {
}
