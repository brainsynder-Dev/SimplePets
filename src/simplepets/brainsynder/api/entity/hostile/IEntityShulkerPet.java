package simplepets.brainsynder.api.entity.hostile;


import simplepets.brainsynder.api.entity.IColorable;

public interface IEntityShulkerPet extends IColorable {
    boolean isClosed();

    void setClosed(boolean var);
}
