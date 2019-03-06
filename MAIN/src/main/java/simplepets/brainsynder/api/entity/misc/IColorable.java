package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

public interface IColorable extends IEntityPet {
    DyeColorWrapper getColor();

    void setColor(DyeColorWrapper i);
}
