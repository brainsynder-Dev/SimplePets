package simplepets.brainsynder.api.entity.passive;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.pet.PetType;

@SupportedVersion(version = ServerVersion.v1_17_R1)
@EntityPetType(petType = PetType.GOAT)
public interface IEntityGoatPet extends IAgeablePet {
    /**
     * According to {@link https://minecraft.gamepedia.com/Goat}
     * goats lose a horn each time they ram a block
     */
    int getHornCount ();
    void setHornCount (int count);

    // Might remove these methods if the info holds true
    boolean isMissingHorns ();
    void setMissingHorns (boolean horns);
}
