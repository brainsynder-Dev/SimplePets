package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IRaider;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.VINDICATOR)
public interface IEntityVindicatorPet extends IEntityPet, IRaider {
    boolean isJohnny();

    void setJohnny(boolean var);
}
