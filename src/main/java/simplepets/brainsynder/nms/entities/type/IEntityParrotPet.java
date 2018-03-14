package simplepets.brainsynder.nms.entities.type;

import simplepets.brainsynder.nms.entities.type.main.IFlyablePet;
import simplepets.brainsynder.nms.entities.type.main.ITameable;
import simplepets.brainsynder.wrapper.ParrotVariant;

public interface IEntityParrotPet extends ITameable, IFlyablePet {
    ParrotVariant getVariant();

    void setVariant(ParrotVariant variant);
}
