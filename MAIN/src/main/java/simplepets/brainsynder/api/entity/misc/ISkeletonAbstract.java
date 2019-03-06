package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface ISkeletonAbstract extends IEntityPet {
    default boolean isArmsRaised() { return false; }

    default void setArmsRaised(boolean flag) {}
}
