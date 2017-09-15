package simplepets.brainsynder.nms.entities.type;

import simplepets.brainsynder.nms.entities.type.main.IAgeablePet;

public interface IEntityPigPet extends IAgeablePet {
    boolean hasSaddle();

    void setSaddled(boolean flag);
}
