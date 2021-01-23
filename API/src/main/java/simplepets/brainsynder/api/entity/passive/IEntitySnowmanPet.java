package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.SNOWMAN)
public interface IEntitySnowmanPet extends IEntityPet {
    boolean hasPumpkin();

    void setHasPumpkin(boolean flag);
}
