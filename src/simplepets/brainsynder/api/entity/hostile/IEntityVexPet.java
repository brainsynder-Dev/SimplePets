package simplepets.brainsynder.api.entity.hostile;


import simplepets.brainsynder.api.entity.IEntityNoClipPet;
import simplepets.brainsynder.api.entity.IFlyablePet;

public interface IEntityVexPet extends IFlyablePet, IEntityNoClipPet {
    boolean isPowered();

    void setPowered(boolean var1);
}
