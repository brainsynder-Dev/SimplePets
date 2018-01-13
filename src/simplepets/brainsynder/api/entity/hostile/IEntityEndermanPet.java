package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface IEntityEndermanPet extends IEntityPet {
    boolean isScreaming();

    void setScreaming(boolean flag);
}
