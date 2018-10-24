package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.IFlyablePet;
import simplepets.brainsynder.api.entity.IRainbow;
import simplepets.brainsynder.api.entity.ITameable;
import simplepets.brainsynder.wrapper.ParrotVariant;

public interface IEntityParrotPet extends ITameable, IFlyablePet, IRainbow {
    ParrotVariant getVariant();

    void setVariant(ParrotVariant variant);
}
