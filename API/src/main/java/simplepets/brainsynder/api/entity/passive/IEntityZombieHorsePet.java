package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IHorseAbstract;
import simplepets.brainsynder.api.entity.misc.ISpecialRiding;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.ZOMBIE_HORSE)
public interface IEntityZombieHorsePet extends IHorseAbstract, ISpecialRiding {
}
