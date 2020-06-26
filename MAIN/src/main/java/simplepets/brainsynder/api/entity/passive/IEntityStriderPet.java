package simplepets.brainsynder.api.entity.passive;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.ISaddle;

/*
DataWatchers:
- BOOST_TIME
- COLD
- SADDLED
 */
@SupportedVersion(version = ServerVersion.v1_16_R1)
public interface IEntityStriderPet extends IAgeablePet, ISaddle {
    boolean isCold();
    void setCold(boolean cold);
}
