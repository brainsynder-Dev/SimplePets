package simplepets.brainsynder.nms.entities.type;

import simplepets.brainsynder.nms.entities.type.main.IEntityPet;

public interface IEntityWitherPet extends IEntityPet {
    boolean isShielded();

    void setShielded(boolean var);

    default boolean isSmall() {
        return false;
    }

    default void setSmall(boolean var) {
    }
}
