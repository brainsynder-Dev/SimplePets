package simplepets.brainsynder.api.entity.passive;

import lib.brainsynder.utils.DyeColorWrapper;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IEntityFishPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.wrappers.TropicalPattern;

@EntityPetType(petType = PetType.TROPICAL_FISH)
public interface IEntityTropicalFishPet extends IEntityFishPet {
    DyeColorWrapper getPatternColor();
    void setPatternColor(DyeColorWrapper var1);

    DyeColorWrapper getBodyColor();
    void setBodyColor(DyeColorWrapper var1);

    TropicalPattern getPattern();
    void setPattern(TropicalPattern var1);
}
