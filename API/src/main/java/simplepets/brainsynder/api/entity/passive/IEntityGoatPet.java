package simplepets.brainsynder.api.entity.passive;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.pet.PetType;

@SupportedVersion(version = ServerVersion.v1_17_R1)
@EntityPetType(petType = PetType.GOAT)
public interface IEntityGoatPet extends IAgeablePet {}
