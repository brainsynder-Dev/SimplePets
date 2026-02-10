package simplepets.brainsynder.api.entity.passive;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.pet.PetType;

@SupportedVersion(version = ServerVersion.v1_21_11)
@EntityPetType(petType = PetType.CAMEL_HUSK)
public interface IEntityCamelHuskPet extends IEntityCamelPet {
}