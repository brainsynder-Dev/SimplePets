package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface IFlag extends IEntityPet {
    void setSpecialFlag(int flag, boolean value);
    boolean getSpecialFlag(int flag);
}
