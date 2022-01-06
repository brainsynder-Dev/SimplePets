package simplepets.brainsynder.api.entity.passive;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.pet.PetType;

@SupportedVersion(version = ServerVersion.v1_17)
@EntityPetType(petType = PetType.GOAT)
public interface IEntityGoatPet extends IAgeablePet {
    /**
     * According to {@link https://minecraft.gamepedia.com/Goat}
     * goats lose a horn each time they ram a block
     *
     * EDIT (6/9/2021): 1.17 does not have a way to change this data yet, hope it is not bedrock only...
     */
//    int getHornCount ();
//    void setHornCount (int count);

    boolean isScreaming ();
    void setScreaming (boolean screaming);
}
