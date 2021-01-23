package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.wrappers.MooshroomType;

@EntityPetType(petType = PetType.MOOSHROOM)
public interface IEntityMooshroomPet extends IAgeablePet {
    void setMooshroomType (MooshroomType type);
    MooshroomType getMooshroomType ();
}
