package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IColorable;
import simplepets.brainsynder.api.entity.misc.IRainbow;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.SHULKER)
public interface IEntityShulkerPet extends IColorable, IRainbow {
    boolean isClosed();

    void setClosed(boolean var);
}
