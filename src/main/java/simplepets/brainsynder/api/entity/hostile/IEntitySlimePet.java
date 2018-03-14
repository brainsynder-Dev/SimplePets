package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface IEntitySlimePet extends IEntityPet {
    int getSize();

    void setSize(int i);

    boolean isSmall();
}
