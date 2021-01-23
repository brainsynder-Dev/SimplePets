package simplepets.brainsynder.api.entity.hostile;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.IShaking;
import simplepets.brainsynder.api.pet.PetType;

/*
DataWatchers:
- BABY
- IMMUNE_TO_ZOMBIFICATION
- CHARGING
- DANCING
 */
@EntityPetType(petType = PetType.PIGLIN)
@SupportedVersion(version = ServerVersion.v1_16_R1)
public interface IEntityPiglinPet extends IAgeablePet, IShaking {
    boolean isCharging ();
    void setCharging (boolean charging);

    boolean isDancing ();
    void setDancing (boolean dancing);
}
