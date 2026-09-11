package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.wrappers.GolemCrackLevel;

@EntityPetType(petType = PetType.IRON_GOLEM)
public interface IEntityIronGolemPet extends IEntityPet {
    GolemCrackLevel getCrackLevel();

    void setCrackLevel(GolemCrackLevel level);
}
