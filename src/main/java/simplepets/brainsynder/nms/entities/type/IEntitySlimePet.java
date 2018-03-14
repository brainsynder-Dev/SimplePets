package simplepets.brainsynder.nms.entities.type;

import simplepets.brainsynder.nms.entities.type.main.IEntityPet;

public interface IEntitySlimePet extends IEntityPet {
    int getSize();

    void setSize(int i);

    boolean isSmall();
}
