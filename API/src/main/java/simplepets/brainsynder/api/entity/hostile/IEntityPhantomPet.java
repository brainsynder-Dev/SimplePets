package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IFlyableEntity;
import simplepets.brainsynder.api.entity.misc.ISizable;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.PHANTOM)
public interface IEntityPhantomPet extends ISizable,IFlyableEntity {
}
