package simplepets.brainsynder.api.entity.hostile;


import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.IFlyablePet;

public interface IEntityPhantomPet extends IEntityPet, IFlyablePet {
    int getSize();

    void setSize(int size);
}
