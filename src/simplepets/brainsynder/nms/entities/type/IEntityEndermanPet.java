package simplepets.brainsynder.nms.entities.type;

import simplepets.brainsynder.nms.entities.type.main.IEntityPet;

public interface IEntityEndermanPet extends IEntityPet {
    boolean isScreaming();

    void setScreaming(boolean flag);
}
