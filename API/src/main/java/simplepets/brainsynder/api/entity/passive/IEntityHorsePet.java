package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IHorseAbstract;
import simplepets.brainsynder.api.entity.misc.ISpecialRiding;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.wrappers.horse.HorseArmorType;
import simplepets.brainsynder.api.wrappers.horse.HorseColorType;
import simplepets.brainsynder.api.wrappers.horse.HorseStyleType;

@EntityPetType(petType = PetType.HORSE)
public interface IEntityHorsePet extends IHorseAbstract, ISpecialRiding {
    HorseArmorType getArmor();

    void setArmor(HorseArmorType var1);

    HorseStyleType getStyle();

    void setStyle(HorseStyleType var1);

    HorseColorType getColor();

    void setColor(HorseColorType var1);
}
