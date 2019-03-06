package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.ITameable;

public interface IEntityOcelotPet extends ITameable {
    default int getCatType() { return 0; }

    default void setCatType(int i) {}
}
