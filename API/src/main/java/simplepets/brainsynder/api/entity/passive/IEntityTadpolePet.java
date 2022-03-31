package simplepets.brainsynder.api.entity.passive;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IEntityFishPet;
import simplepets.brainsynder.api.pet.PetType;

@SupportedVersion(version = ServerVersion.v1_19)
@EntityPetType(petType = PetType.TADPOLE)
public interface IEntityTadpolePet extends IEntityFishPet {
}
