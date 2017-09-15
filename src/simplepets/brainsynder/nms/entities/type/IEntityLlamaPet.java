package simplepets.brainsynder.nms.entities.type;

import simplepets.brainsynder.nms.entities.type.main.IChestedAbstractPet;
import simplepets.brainsynder.nms.entities.type.main.IColorable;
import simplepets.brainsynder.wrapper.LlamaColor;


public interface IEntityLlamaPet extends IChestedAbstractPet, IColorable {
    void setSkinColor(LlamaColor skinColor);

    LlamaColor getLlamaColor();
}