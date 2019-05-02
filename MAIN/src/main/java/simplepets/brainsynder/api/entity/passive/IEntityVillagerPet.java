package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.wrapper.ProfessionWrapper;
import simplepets.brainsynder.wrapper.villager.VillagerData;

public interface IEntityVillagerPet extends IAgeablePet {

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

    default ProfessionWrapper getProfession(){ return ProfessionWrapper.FARMER; }

    default void setProfession(ProfessionWrapper wrapper){}
}
