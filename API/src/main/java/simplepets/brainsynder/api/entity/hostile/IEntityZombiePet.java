package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.IShaking;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.ZOMBIE)
public interface IEntityZombiePet extends IAgeablePet, IShaking {
    void setArmsRaised(boolean flag);
    boolean isArmsRaised();
}
