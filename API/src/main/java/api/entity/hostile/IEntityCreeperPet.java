package api.entity.hostile;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.CREEPER)
public interface IEntityCreeperPet extends IEntityPet {
    boolean isPowered();

    void setPowered(boolean flag);

    default boolean isIgnited() { return false; }

    default void setIgnited(boolean flag) {}
}
