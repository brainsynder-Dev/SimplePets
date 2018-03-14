package simplepets.brainsynder.pet;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import simple.brainsynder.storage.IStorage;
import simplepets.brainsynder.menu.MenuItem;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.wrapper.EntityWrapper;

public interface IPet {
    Player getOwner();

    void removePet(boolean var1);

    void teleportToOwner();

    void teleport(Location var1);

    PetType getPetType();

    EntityWrapper getEntityType();

    IEntityPet getEntity();

    IEntityPet getVisableEntity();

    /**
     * @Deprecated Will be removed when MC version 1.13 is released
     */
    @Deprecated
    Entity getDisplayEntity();

    IStorage<MenuItem> getItems();

    boolean isHat();

    void setHat(boolean isHat);

    boolean isVehicle();

    void setVehicle(boolean vehicle);

    void ridePet();

    void hatPet();
}
