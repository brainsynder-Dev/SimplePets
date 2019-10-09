package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.IProfession;
import simplepets.brainsynder.wrapper.ProfessionWrapper;

public interface IEntityVillagerPet extends IAgeablePet, IProfession {
    default ProfessionWrapper getProfession(){ return ProfessionWrapper.FARMER; }

    default void setProfession(ProfessionWrapper wrapper){}
}
