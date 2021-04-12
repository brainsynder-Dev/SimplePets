package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IPowered;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.CREEPER)
public interface IEntityCreeperPet extends IEntityPet, IPowered {
    default boolean isIgnited() { return false; }

    default void setIgnited(boolean flag) {}
}
