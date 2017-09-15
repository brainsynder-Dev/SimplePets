package simplepets.brainsynder.nms.entities.type;

import simplepets.brainsynder.nms.entities.type.main.IEntityPet;

public interface IEntityGuardianPet extends IEntityPet {
    boolean isElder();

    void setElder(boolean var1);
}
