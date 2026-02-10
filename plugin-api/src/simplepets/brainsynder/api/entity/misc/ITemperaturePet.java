package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.wrappers.TemperatureVariant;

public interface ITemperaturePet extends IEntityPet {
    default TemperatureVariant getVariant() {
        return TemperatureVariant.TEMPERATE;
    }

     default void setVariant(TemperatureVariant variant) {}
}
