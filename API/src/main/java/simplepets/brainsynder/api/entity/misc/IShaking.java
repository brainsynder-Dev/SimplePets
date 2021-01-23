package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface IShaking extends IEntityPet {
    boolean isShaking();

    void setShaking(boolean shaking);
}
