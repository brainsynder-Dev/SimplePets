package simplepets.brainsynder.api.entity.hostile;


import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IFlyableEntity;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.BLAZE)
public interface IEntityBlazePet extends IEntityPet, IFlyableEntity {
}
