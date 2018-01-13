package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.IHorseAbstract;
import simplepets.brainsynder.wrapper.HorseArmorType;
import simplepets.brainsynder.wrapper.HorseColorType;
import simplepets.brainsynder.wrapper.HorseStyleType;

public interface IEntityHorsePet extends IHorseAbstract {
    /**
     * @Deprecated Will be removed when MC version 1.13 is released
     */
    @Deprecated
    default void setVariant(HorseColorType var1) {}

    HorseArmorType getArmor();

    void setArmor(HorseArmorType var1);

    HorseStyleType getStyle();

    void setStyle(HorseStyleType var1);

    HorseColorType getColor();

    void setColor(HorseColorType var1);
}
