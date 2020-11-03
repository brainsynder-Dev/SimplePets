package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.IHorseAbstract;
import simplepets.brainsynder.api.entity.misc.ISpecialRiding;
import simplepets.brainsynder.wrapper.HorseArmorType;
import simplepets.brainsynder.wrapper.HorseColorType;
import simplepets.brainsynder.wrapper.HorseStyleType;

public interface IEntityHorsePet extends IHorseAbstract, ISpecialRiding {
    HorseArmorType getArmor();

    void setArmor(HorseArmorType var1);

    HorseStyleType getStyle();

    void setStyle(HorseStyleType var1);

    HorseColorType getColor();

    void setColor(HorseColorType var1);
}
