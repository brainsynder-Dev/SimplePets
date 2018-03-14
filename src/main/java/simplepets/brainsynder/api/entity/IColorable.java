package simplepets.brainsynder.api.entity;

import simplepets.brainsynder.wrapper.DyeColorWrapper;

public interface IColorable extends IEntityPet {
    DyeColorWrapper getColor();

    void setColor(DyeColorWrapper i);
}
