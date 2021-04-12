package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface ISizable extends IEntityPet {
    int getSize();

    void setSize(int i);

    default boolean isSmall() {
        return getSize() <= 1;
    }
}
