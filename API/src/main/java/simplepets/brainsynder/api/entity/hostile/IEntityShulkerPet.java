package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IDisplayEntity;
import simplepets.brainsynder.api.entity.misc.IRainbow;
import simplepets.brainsynder.api.entity.misc.IResetColor;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.SHULKER)
public interface IEntityShulkerPet extends IResetColor, IRainbow, IDisplayEntity {
    boolean isShulkerClosed();

    void setShulkerClosed(boolean var);
}
