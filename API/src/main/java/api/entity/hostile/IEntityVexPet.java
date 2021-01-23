package api.entity.hostile;


import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IEntityNoClipPet;
import simplepets.brainsynder.api.entity.misc.IFlyableEntity;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.VEX)
public interface IEntityVexPet extends IFlyableEntity, IEntityNoClipPet {
    boolean isPowered();

    void setPowered(boolean var1);
}
