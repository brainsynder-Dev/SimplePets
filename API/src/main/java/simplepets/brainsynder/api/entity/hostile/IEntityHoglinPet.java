package simplepets.brainsynder.api.entity.hostile;


import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.IShaking;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.HOGLIN)
@SupportedVersion(version = ServerVersion.v1_16_R1)
public interface IEntityHoglinPet extends IAgeablePet, IShaking {}
