package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.misc.IAgeablePet;

/*
DataWatchers:
- BABY
- ZOMBIE_TYPE
- CONVERTING
 */
public interface IEntityZombifiedPiglinPet extends IAgeablePet {
    default void setArmsRaised(boolean flag) {}
    default boolean isArmsRaised() { return false; }

    default boolean isShaking () { return false; }
    default void setShaking (boolean value) {}
}
