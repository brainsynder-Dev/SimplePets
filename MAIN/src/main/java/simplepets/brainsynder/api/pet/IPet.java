package simplepets.brainsynder.api.pet;

import org.bukkit.entity.Player;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItem;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

import java.util.List;

public interface IPet {
    Player getOwner();

    PetType getPetType();

    EntityWrapper getEntityType();

    List<MenuItem> getItems();

    void removePet(boolean var1);

    IEntityPet getEntity();

    IEntityPet getVisableEntity();

    boolean isHat();

    void setHat(boolean isHat);

    boolean isVehicle();

    boolean setVehicle(boolean vehicle, boolean byEvent);

    void toggleRiding(boolean byEvent);

    void toggleHat();
}
