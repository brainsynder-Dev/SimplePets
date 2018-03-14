package simplepets.brainsynder.nms.entities.type;

import simplepets.brainsynder.nms.entities.type.main.IAgeablePet;

public interface IEntityPolarBearPet extends IAgeablePet {
    void setStandingUp(boolean flag);

    boolean isStanding();
}
