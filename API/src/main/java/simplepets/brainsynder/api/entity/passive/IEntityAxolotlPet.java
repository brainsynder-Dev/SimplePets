package simplepets.brainsynder.api.entity.passive;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.wrappers.AxolotlVariant;

@SupportedVersion(version = ServerVersion.v1_17)
@EntityPetType(petType = PetType.AXOLOTL)
public interface IEntityAxolotlPet extends IAgeablePet {

    boolean isPlayingDead();
    void setPlayingDead(boolean playingDead);

    AxolotlVariant getVariant ();
    void setVariant (AxolotlVariant variant);

}
