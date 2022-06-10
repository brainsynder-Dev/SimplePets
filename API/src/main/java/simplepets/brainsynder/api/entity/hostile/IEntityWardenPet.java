package simplepets.brainsynder.api.entity.hostile;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.wrappers.AngerLevel;

@SupportedVersion(version = ServerVersion.v1_19)
@EntityPetType(petType = PetType.WARDEN)
public interface IEntityWardenPet extends IEntityPet {

    void setAngerLevel (AngerLevel level);
    AngerLevel getAngerLevel ();

    void setVibrationEffect (boolean value);
    boolean getVibrationEffect ();
}
