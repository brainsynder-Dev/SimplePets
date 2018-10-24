package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface IEntityBatPet extends IEntityPet {
    boolean isHanging();

    void setHanging(boolean var1);
}
