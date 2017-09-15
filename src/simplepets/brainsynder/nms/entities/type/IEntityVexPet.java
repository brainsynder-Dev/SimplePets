package simplepets.brainsynder.nms.entities.type;


import simplepets.brainsynder.nms.entities.type.main.IEntityNoClipPet;
import simplepets.brainsynder.nms.entities.type.main.IFlyablePet;

public interface IEntityVexPet extends IFlyablePet, IEntityNoClipPet {
    boolean isPowered();

    void setPowered(boolean var1);
}
