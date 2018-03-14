package simplepets.brainsynder.nms.entities.type;


import simplepets.brainsynder.nms.entities.type.main.IColorable;
import simplepets.brainsynder.nms.entities.type.main.ITameable;

public interface IEntityWolfPet extends ITameable,
        IColorable {

    default boolean isHeadTilted() {
        return false;
    }

    default void setHeadTilted (boolean var) {}

    default boolean isAngry() {
        return false;
    }

    default void setAngry (boolean var) {}
}
