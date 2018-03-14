package simplepets.brainsynder.nms.entities.type;


import simplepets.brainsynder.nms.entities.type.main.IAgeablePet;
import simplepets.brainsynder.nms.entities.type.main.IColorable;

public interface IEntitySheepPet extends IAgeablePet,
        IColorable {
    boolean isSheared();

    void setSheared(boolean flag);
}
