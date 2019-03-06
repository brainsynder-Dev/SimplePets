package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface IAgeablePet extends IEntityPet {
    default boolean isBaby(){
        return false;
    }

    default void setBaby(boolean flag){}
}
