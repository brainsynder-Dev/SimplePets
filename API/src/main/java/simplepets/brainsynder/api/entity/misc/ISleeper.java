package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface ISleeper extends IEntityPet {
    boolean isPetSleeping();

    void setPetSleeping(boolean sleeping);
}
