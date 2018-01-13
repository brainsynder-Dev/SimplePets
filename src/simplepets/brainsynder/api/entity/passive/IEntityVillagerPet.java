package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.IAgeablePet;
import simplepets.brainsynder.wrapper.ProfessionWrapper;

public interface IEntityVillagerPet extends IAgeablePet {

    ProfessionWrapper getProfession();

    void setProfession(ProfessionWrapper wrapper);
}
