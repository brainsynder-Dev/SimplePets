package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.IFishPet;

public interface IEntityPufferFishPet extends IFishPet {
    int getPuffState();

    void setPuffState(int var1);
}
