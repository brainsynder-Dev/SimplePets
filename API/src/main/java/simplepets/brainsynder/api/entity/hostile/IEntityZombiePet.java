package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.IShaking;
import simplepets.brainsynder.api.entity.passive.IEntityVillagerPet;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.ZOMBIE)
public interface IEntityZombiePet extends IAgeablePet, IEntityVillagerPet, IShaking {
    void setArmsRaised(boolean flag);
    boolean isArmsRaised();
}
