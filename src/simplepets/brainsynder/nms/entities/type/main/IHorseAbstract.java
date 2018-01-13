package simplepets.brainsynder.nms.entities.type.main;

import simplepets.brainsynder.wrapper.HorseColorType;
import simplepets.brainsynder.wrapper.HorseStyleType;

public interface IHorseAbstract extends IAgeablePet {
    boolean isSaddled();

    void setSaddled(boolean var1);

    /**
     * @Deprecated Will be removed when MC version 1.13 is released
     */
    @Deprecated
    void setVariant(HorseColorType var1);

    /**
     * @Deprecated Will be removed when MC version 1.13 is released
     */
    @Deprecated
    void setVariant(HorseColorType var1, HorseStyleType var2);
}
