package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IRaider;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.WITCH)
public interface IEntityWitchPet extends IEntityPet, IRaider {
    void setDrinkingPotion(boolean flag);
    boolean isDrinkingPotion();
}
