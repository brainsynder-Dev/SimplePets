package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.IAgeablePet;

public interface IEntityPigPet extends IAgeablePet {
    boolean hasSaddle();

    void setSaddled(boolean flag);
}
