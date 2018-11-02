package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.IAgeablePet;
import simplepets.brainsynder.wrapper.ProfessionWrapper;

public interface IEntityVillagerPet extends IAgeablePet {

    default ProfessionWrapper getProfession(){ return ProfessionWrapper.FARMER; }

    default void setProfession(ProfessionWrapper wrapper){}
}
