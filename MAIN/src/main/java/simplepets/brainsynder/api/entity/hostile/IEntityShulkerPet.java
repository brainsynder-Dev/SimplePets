package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.misc.IColorable;
import simplepets.brainsynder.api.entity.misc.IRainbow;

public interface IEntityShulkerPet extends IColorable, IRainbow {
    boolean isClosed();

    void setClosed(boolean var);
}
