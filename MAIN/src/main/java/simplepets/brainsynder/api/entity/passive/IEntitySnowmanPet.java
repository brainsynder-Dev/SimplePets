package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface IEntitySnowmanPet extends IEntityPet {
    boolean hasPumpkin();

    void setHasPumpkin(boolean flag);
}
