package simplepets.brainsynder.api.entity.passive;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.IFlyableEntity;
import simplepets.brainsynder.api.entity.misc.IResetColor;
import simplepets.brainsynder.api.pet.PetType;

@SupportedVersion(version = ServerVersion.v1_21_6)
@EntityPetType(petType = PetType.HAPPY_GHAST)
public interface IEntityHappyGhastPet extends IAgeablePet, IResetColor, IFlyableEntity {}
