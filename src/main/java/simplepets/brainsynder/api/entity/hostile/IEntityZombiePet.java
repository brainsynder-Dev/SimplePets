package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IAgeablePet;

public interface IEntityZombiePet extends IAgeablePet {
    default void setVillager(boolean flag) {}
}
