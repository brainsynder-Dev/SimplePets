package simplepets.brainsynder.api.entity.misc;

import lib.brainsynder.utils.DyeColorWrapper;
import simplepets.brainsynder.api.entity.IEntityPet;

public interface IColorable extends IEntityPet {
    DyeColorWrapper getColor();

    void setColor(DyeColorWrapper i);
}
