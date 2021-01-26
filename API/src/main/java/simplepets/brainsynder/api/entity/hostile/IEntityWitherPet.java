package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.WITHER)
public interface IEntityWitherPet extends IEntityPet {
    boolean isShielded();

    void setShielded(boolean var);

    boolean isSmall();

    void setSmall(boolean var);
}
