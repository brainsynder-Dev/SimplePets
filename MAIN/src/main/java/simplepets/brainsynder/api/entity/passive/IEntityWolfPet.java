package simplepets.brainsynder.api.entity.passive;


import simplepets.brainsynder.api.entity.IColorable;
import simplepets.brainsynder.api.entity.ITameable;

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
