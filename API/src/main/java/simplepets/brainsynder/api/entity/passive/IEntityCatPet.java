package simplepets.brainsynder.api.entity.passive;

import lib.brainsynder.utils.DyeColorWrapper;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.ISleeper;
import simplepets.brainsynder.api.entity.misc.ITameable;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.wrappers.CatType;

@EntityPetType(petType = PetType.CAT)
public interface IEntityCatPet extends ITameable, ISleeper {
    CatType getCatType ();
    void setCatType (CatType type);

    DyeColorWrapper getCollarColor();
    void setCollarColor(DyeColorWrapper color);

    boolean isHeadUp ();
    void setHeadUp (boolean value);
}
