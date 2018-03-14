package simplepets.brainsynder.nms.entities.type.main;

import org.bukkit.entity.Entity;
import simplepets.brainsynder.utils.IStandMethod;

public interface IEntityControllerPet extends IEntityPet {
    Entity getDisplayEntity();

    void setDisplayEntity(Entity entity);

    void remove();

    void reloadLocation();

    boolean isMoving();

    default void updateName() {}

    void onStopWalking(IStandMethod standMethod);

    void onStartWalking(IStandMethod standMethod);

    IEntityPet getVisibleEntity();
}
