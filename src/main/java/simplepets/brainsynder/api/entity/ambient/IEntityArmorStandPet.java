package simplepets.brainsynder.api.entity.ambient;

import simplepets.brainsynder.api.entity.IDisplayEntity;

public interface IEntityArmorStandPet extends IDisplayEntity {
    boolean isSmall();

    void setSmall(boolean flag);

    boolean isOwner();

    void setOwner(boolean flag);

}
