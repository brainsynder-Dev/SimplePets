package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface IEntityGiantPet extends IEntityPet {
    @Override
    default boolean isBig() {
        return true;
    }
}
