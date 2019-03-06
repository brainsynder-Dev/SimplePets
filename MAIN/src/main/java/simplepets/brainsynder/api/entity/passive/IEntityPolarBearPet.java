package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.IAgeablePet;

public interface IEntityPolarBearPet extends IAgeablePet {
    void setStandingUp(boolean flag);

    boolean isStanding();
}
