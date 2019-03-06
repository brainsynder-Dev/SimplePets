package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface IFlag extends IEntityPet {
    void setFlag(int flag, boolean value);
    boolean getFlag(int flag);
}
