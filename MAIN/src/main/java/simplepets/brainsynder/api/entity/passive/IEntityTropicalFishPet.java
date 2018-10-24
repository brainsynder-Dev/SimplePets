package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.IEntityFishPet;
import simplepets.brainsynder.wrapper.DyeColorWrapper;
import simplepets.brainsynder.wrapper.TropicalPattern;

public interface IEntityTropicalFishPet extends IEntityFishPet {
    DyeColorWrapper getPatternColor();
    void setPatternColor(DyeColorWrapper var1);

    DyeColorWrapper getBodyColor();
    void setBodyColor(DyeColorWrapper var1);

    TropicalPattern getPattern();
    void setPattern(TropicalPattern var1);
}
