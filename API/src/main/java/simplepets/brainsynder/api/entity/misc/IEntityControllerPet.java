package simplepets.brainsynder.api.entity.misc;

import org.bukkit.entity.Entity;
import simplepets.brainsynder.api.entity.IEntityPet;

import java.util.Optional;

public interface IEntityControllerPet extends IEntityPet {
    Optional<Entity> getDisplayEntity();

    Optional<Entity> getDisplayRider ();

    void setDisplayEntity(Entity entity);

    void remove();

    void reloadLocation();

    void addPassenger(Entity passenger);

    boolean isMoving();

    void updateName();

    IEntityPet getVisibleEntity();
}