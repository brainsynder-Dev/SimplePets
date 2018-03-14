package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IAgeablePet;
import simplepets.brainsynder.wrapper.ProfessionWrapper;

public interface IEntityZombieVillagerPet extends IAgeablePet {

    ProfessionWrapper getProfession();

    void setProfession(ProfessionWrapper wrapper);
}
