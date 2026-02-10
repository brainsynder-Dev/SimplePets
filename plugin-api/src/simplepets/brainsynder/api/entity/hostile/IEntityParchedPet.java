package simplepets.brainsynder.api.entity.hostile;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.pet.PetType;

@SupportedVersion(version = ServerVersion.v1_21_11)
@EntityPetType(petType = PetType.PARCHED)
public interface IEntityParchedPet extends IEntitySkeletonPet {
}
