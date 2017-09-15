package simplepets.brainsynder.nms.entities.type;


import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.nms.entities.type.main.IFlyablePet;

public interface IEntityBlazePet extends IEntityPet, IFlyablePet {
    boolean isBurning();

    void setBurning(boolean var);
}
