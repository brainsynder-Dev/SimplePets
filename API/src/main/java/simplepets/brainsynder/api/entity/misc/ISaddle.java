package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface ISaddle extends IEntityPet {
    default boolean isSaddled(){
        return false;
    }

    default void setSaddled(boolean shaking){}
}
