package simplepets.brainsynder.nms.entities.type;

import simplepets.brainsynder.nms.entities.type.main.IEntityPet;

public interface IEntityGiantPet extends IEntityPet {
    @Override
    default boolean isBig() {
        return true;
    }
}
