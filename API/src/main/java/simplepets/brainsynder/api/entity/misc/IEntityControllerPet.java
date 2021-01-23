package simplepets.brainsynder.api.entity.misc;

import org.bukkit.entity.Entity;
import simplepets.brainsynder.api.entity.IEntityPet;

public interface IEntityControllerPet extends IEntityPet {
    Entity getDisplayEntity();

    default Entity getDisplayRider () {
        return null;
    }

    void setDisplayEntity(Entity entity);

    void remove();

    void reloadLocation();

    void addPassenger(Entity passenger);

    boolean isMoving();

    default void updateName() {}

    IEntityPet getVisibleEntity();
}