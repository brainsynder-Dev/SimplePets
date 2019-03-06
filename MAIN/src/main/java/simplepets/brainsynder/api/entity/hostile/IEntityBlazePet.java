package simplepets.brainsynder.api.entity.hostile;


import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IFlyablePet;

public interface IEntityBlazePet extends IEntityPet, IFlyablePet {
    boolean isBurning();

    void setBurning(boolean var);
}
