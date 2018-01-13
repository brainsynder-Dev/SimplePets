package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.IChestedAbstractPet;
import simplepets.brainsynder.api.entity.IColorable;
import simplepets.brainsynder.wrapper.LlamaColor;


public interface IEntityLlamaPet extends IChestedAbstractPet, IColorable {
    void setSkinColor(LlamaColor skinColor);

    LlamaColor getLlamaColor();
}