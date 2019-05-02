package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.wrapper.MooshroomType;

public interface IEntityMooshroomPet extends IAgeablePet {

    // These are used for mc: 1.14
    default void setMooshroomType (MooshroomType type) {}
    default MooshroomType getMooshroomType () { return MooshroomType.RED; }
}
