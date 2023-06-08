package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface ISitting extends IEntityPet {
    boolean isSitting();
    void setSitting(boolean sitting);
}
