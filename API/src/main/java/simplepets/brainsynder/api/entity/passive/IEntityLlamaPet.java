package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IChestedAbstractPet;
import simplepets.brainsynder.api.entity.misc.IResetColor;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.wrappers.LlamaColor;

@EntityPetType(petType = PetType.LLAMA)
public interface IEntityLlamaPet extends IChestedAbstractPet, IResetColor {
    void setSkinColor(LlamaColor skinColor);

    LlamaColor getSkinColor();
}