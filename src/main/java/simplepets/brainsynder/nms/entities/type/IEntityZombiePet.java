package simplepets.brainsynder.nms.entities.type;

import simplepets.brainsynder.nms.entities.type.main.IAgeablePet;

public interface IEntityZombiePet extends IAgeablePet {
    default void setVillager(boolean flag) {}
}
