package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.ITameable;
import simplepets.brainsynder.wrapper.CatType;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

public interface IEntityCatPet extends ITameable {
    CatType getCatType ();
    void setCatType (CatType type);

    DyeColorWrapper getCollarColor();
    void setCollarColor(DyeColorWrapper color);

    boolean isHeadUp ();
    void setHeadUp (boolean value);
}
