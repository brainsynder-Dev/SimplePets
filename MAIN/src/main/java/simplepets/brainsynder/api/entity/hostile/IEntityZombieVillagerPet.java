package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.wrapper.villager.VillagerData;

public interface IEntityZombieVillagerPet extends IEntityZombiePet {

    /**
     * This gets used for 1.14+
     */
    default VillagerData getVillagerData () {
        return null;
    }

    /**
     * This gets used for 1.14+
     */
    default void setVillagerData (VillagerData data){}
}
