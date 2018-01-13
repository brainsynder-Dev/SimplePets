package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.ITameable;

public interface IEntityOcelotPet extends ITameable {
    int getCatType();

    void setCatType(int i);
}
