package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface IEntityWitherPet extends IEntityPet {
    boolean isShielded();

    void setShielded(boolean var);

    default boolean isSmall() {
        return false;
    }

    default void setSmall(boolean var) {
    }
}
