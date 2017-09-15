package simplepets.brainsynder.nms.entities.type;

import simplepets.brainsynder.nms.entities.type.main.IEntityPet;

public interface IEntitySnowmanPet extends IEntityPet {
    boolean hasPumpkin();

    void setHasPumpkin(boolean flag);
}
