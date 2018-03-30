package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IColorable;
import simplepets.brainsynder.api.entity.IRainbow;

public interface IEntityShulkerPet extends IColorable, IRainbow {
    boolean isClosed();

    void setClosed(boolean var);
}
