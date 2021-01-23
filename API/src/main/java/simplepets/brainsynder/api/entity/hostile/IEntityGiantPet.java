package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.GIANT)
public interface IEntityGiantPet extends IEntityPet {
    @Override
    default boolean isBig() {
        return true;
    }
}
