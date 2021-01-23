package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IChestedAbstractPet;
import simplepets.brainsynder.api.entity.misc.ISpecialRiding;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.MULE)
public interface IEntityMulePet extends IChestedAbstractPet, ISpecialRiding {
}
