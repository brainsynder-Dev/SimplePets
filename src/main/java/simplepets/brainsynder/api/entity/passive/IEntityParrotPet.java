package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.IFlyablePet;
import simplepets.brainsynder.api.entity.ITameable;
import simplepets.brainsynder.wrapper.ParrotVariant;

public interface IEntityParrotPet extends ITameable, IFlyablePet {
    ParrotVariant getVariant();

    void setVariant(ParrotVariant variant);
}
