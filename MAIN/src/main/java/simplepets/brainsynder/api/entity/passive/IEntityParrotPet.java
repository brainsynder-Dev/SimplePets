package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.IFlyablePet;
import simplepets.brainsynder.api.entity.misc.IRainbow;
import simplepets.brainsynder.api.entity.misc.ITameable;
import simplepets.brainsynder.wrapper.ParrotVariant;

public interface IEntityParrotPet extends ITameable, IFlyablePet, IRainbow {
    ParrotVariant getVariant();

    void setVariant(ParrotVariant variant);
}
