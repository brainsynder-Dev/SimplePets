package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface IAgeablePet extends IEntityPet {
    boolean isBaby();

    void setBaby(boolean flag);
}
