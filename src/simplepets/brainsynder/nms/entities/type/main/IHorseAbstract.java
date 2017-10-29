package simplepets.brainsynder.nms.entities.type.main;

import simplepets.brainsynder.wrapper.HorseColorType;
import simplepets.brainsynder.wrapper.HorseStyleType;

public interface IHorseAbstract extends IAgeablePet {
    boolean isSaddled();

    void setSaddled(boolean var1);

    @Deprecated
    void setVariant(HorseColorType var1);

    @Deprecated
    void setVariant(HorseColorType var1, HorseStyleType var2);
}
