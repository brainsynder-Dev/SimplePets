package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface IEntityGuardianPet extends IEntityPet {
    default boolean isElder() { return false; }

    default void setElder(boolean var1) {}
}
