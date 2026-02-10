package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface ISaddle extends IEntityPet {
    default boolean isPetSaddled() {
        return false;
    }

    default void setPetSaddled(boolean saddled) {}
}
