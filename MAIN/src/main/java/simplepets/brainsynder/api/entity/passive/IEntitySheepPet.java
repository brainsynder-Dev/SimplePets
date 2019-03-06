package simplepets.brainsynder.api.entity.passive;


import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.IColorable;
import simplepets.brainsynder.api.entity.misc.IRainbow;

public interface IEntitySheepPet extends IAgeablePet, IColorable, IRainbow {
    boolean isSheared();

    void setSheared(boolean flag);
}
