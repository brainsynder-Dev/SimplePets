package simplepets.brainsynder.nms.entities.type;


import simplepets.brainsynder.nms.entities.type.main.IColorable;

public interface IEntityShulkerPet extends IColorable {
    boolean isClosed();

    void setClosed(boolean var);
}
