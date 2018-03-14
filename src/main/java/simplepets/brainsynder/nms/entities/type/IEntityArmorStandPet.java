package simplepets.brainsynder.nms.entities.type;

import simplepets.brainsynder.nms.entities.type.main.IDisplayEntity;

public interface IEntityArmorStandPet extends IDisplayEntity {
    boolean isSmall();

    void setSmall(boolean flag);

    boolean isOwner();

    void setOwner(boolean flag);
}
