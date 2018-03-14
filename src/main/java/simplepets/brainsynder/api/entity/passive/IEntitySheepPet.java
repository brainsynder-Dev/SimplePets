package simplepets.brainsynder.api.entity.passive;


import simplepets.brainsynder.api.entity.IAgeablePet;
import simplepets.brainsynder.api.entity.IColorable;

public interface IEntitySheepPet extends IAgeablePet,
        IColorable {
    boolean isSheared();

    void setSheared(boolean flag);
}
