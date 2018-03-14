package simplepets.brainsynder.nms.entities.type;

import simplepets.brainsynder.nms.entities.type.main.IAgeablePet;
import simplepets.brainsynder.wrapper.ProfessionWrapper;

public interface IEntityZombieVillagerPet extends IAgeablePet {

    ProfessionWrapper getProfession();

    void setProfession(ProfessionWrapper wrapper);
}
