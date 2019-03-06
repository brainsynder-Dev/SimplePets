package simplepets.brainsynder.api.entity.hostile;


import simplepets.brainsynder.api.entity.misc.IEntityNoClipPet;
import simplepets.brainsynder.api.entity.misc.IFlyablePet;

public interface IEntityVexPet extends IFlyablePet, IEntityNoClipPet {
    boolean isPowered();

    void setPowered(boolean var1);
}
