package simplepets.brainsynder.api.entity.passive;


import simplepets.brainsynder.api.entity.IAgeablePet;
import simplepets.brainsynder.api.entity.IColorable;
import simplepets.brainsynder.api.entity.IRainbow;

public interface IEntitySheepPet extends IAgeablePet, IColorable, IRainbow {
    boolean isSheared();

    void setSheared(boolean flag);
}
