package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.wrappers.ColorWrapper;

public interface IResetColor extends IEntityPet {
    ColorWrapper getColorWrapper();

    void setColorWrapper(ColorWrapper i);
}
