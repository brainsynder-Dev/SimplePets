package simplepets.brainsynder.nms.entities.type.main;

import simplepets.brainsynder.wrapper.DyeColorWrapper;

public interface IColorable extends IEntityPet {
    DyeColorWrapper getColor();

    void setColor(DyeColorWrapper i);
}
