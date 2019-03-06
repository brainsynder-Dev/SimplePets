package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.IAgeablePet;

public interface IEntityPigPet extends IAgeablePet {
    boolean hasSaddle();

    void setSaddled(boolean flag);
}
