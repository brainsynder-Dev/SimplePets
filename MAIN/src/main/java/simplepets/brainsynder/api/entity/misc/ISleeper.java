package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface ISleeper extends IEntityPet {
    default boolean isSleeping(){
        return false;
    }

    default void setSleeping(boolean sleeping){}
}
