package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface IPowered extends IEntityPet {
    boolean isPowered();
    void setPowered(boolean powered);
}
