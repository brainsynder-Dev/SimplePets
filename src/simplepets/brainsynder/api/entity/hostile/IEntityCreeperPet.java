package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface IEntityCreeperPet extends IEntityPet {
    boolean isPowered();

    void setPowered(boolean flag);

    default boolean isIgnited() { return false; }

    default void setIgnited(boolean flag) {}
}
