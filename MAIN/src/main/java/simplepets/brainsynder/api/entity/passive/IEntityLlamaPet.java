package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.IChestedAbstractPet;
import simplepets.brainsynder.api.entity.misc.IColorable;
import simplepets.brainsynder.wrapper.LlamaColor;


public interface IEntityLlamaPet extends IChestedAbstractPet, IColorable {
    void setSkinColor(LlamaColor skinColor);

    LlamaColor getLlamaColor();
}