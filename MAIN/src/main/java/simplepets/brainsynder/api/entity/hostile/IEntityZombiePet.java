package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IAgeablePet;
import simplepets.brainsynder.api.entity.passive.IEntityVillagerPet;

public interface IEntityZombiePet extends IAgeablePet, IEntityVillagerPet {
    default void setVillager(boolean flag) {}

    default void setArmsRaised(boolean flag) {}
    default boolean isArmsRaised() { return false; }

}
