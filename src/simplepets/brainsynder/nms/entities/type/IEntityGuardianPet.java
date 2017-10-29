package simplepets.brainsynder.nms.entities.type;

import simplepets.brainsynder.nms.entities.type.main.IEntityPet;

public interface IEntityGuardianPet extends IEntityPet {
    default boolean isElder() { return false; }

    default void setElder(boolean var1) {}
}
