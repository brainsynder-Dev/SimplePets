package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.misc.IEntityFishPet;
import simplepets.brainsynder.wrapper.PufferState;

public interface IEntityPufferFishPet extends IEntityFishPet {
    PufferState getPuffState();

    void setPuffState(PufferState state);
}
