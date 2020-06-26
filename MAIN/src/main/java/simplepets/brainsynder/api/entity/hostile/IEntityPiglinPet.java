package simplepets.brainsynder.api.entity.hostile;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.IShaking;

/*
DataWatchers:
- BABY
- IMMUNE_TO_ZOMBIFICATION
- CHARGING
- DANCING
 */
@SupportedVersion(version = ServerVersion.v1_16_R1)
public interface IEntityPiglinPet extends IAgeablePet, IShaking {
    boolean isCharging ();
    void setCharging (boolean charging);

    boolean isDancing ();
    void setDancing (boolean dancing);
}
