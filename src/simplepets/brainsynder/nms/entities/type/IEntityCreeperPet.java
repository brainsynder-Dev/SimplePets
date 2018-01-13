package simplepets.brainsynder.nms.entities.type;

import simplepets.brainsynder.nms.entities.type.main.IEntityPet;

public interface IEntityCreeperPet extends IEntityPet {
    boolean isPowered();

    void setPowered(boolean flag);

    default boolean isIgnited() { return false; }

    default void setIgnited(boolean flag) {}
}
