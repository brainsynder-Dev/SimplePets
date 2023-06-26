package simplepets.brainsynder.api.entity.passive;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IHorseAbstract;
import simplepets.brainsynder.api.entity.misc.ISitting;
import simplepets.brainsynder.api.entity.misc.ISpecialRiding;
import simplepets.brainsynder.api.pet.PetType;

@SupportedVersion(version = ServerVersion.v1_20)
@EntityPetType(petType = PetType.CAMEL)
public interface IEntityCamelPet extends IHorseAbstract, ISpecialRiding, ISitting {
}