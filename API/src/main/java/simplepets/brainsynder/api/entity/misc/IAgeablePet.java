package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface IAgeablePet extends IEntityPet {
    default boolean isBaby() {
        return isBabySafe();
    }

    default void setBaby(boolean flag) {
        setBabySafe(flag);
    }

    boolean isBabySafe();

    void setBabySafe(boolean flag);
}
