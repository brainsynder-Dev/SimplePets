package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface IShaking extends IEntityPet {
    default boolean isShaking(){
        return false;
    }

    default void setShaking(boolean shaking){}
}
