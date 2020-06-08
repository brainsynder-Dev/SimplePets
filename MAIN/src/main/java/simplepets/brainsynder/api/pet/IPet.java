package simplepets.brainsynder.api.pet;

import org.bukkit.entity.Player;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItem;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

import java.util.List;

public interface IPet {
    Player getOwner();

    void removePet(boolean var1);

    PetType getPetType();

    EntityWrapper getEntityType();

    IEntityPet getEntity();

    IEntityPet getVisableEntity();

    List<MenuItem> getItems();

    boolean isHat();

    void setHat(boolean isHat);

    boolean isVehicle();

    void setVehicle(boolean vehicle, boolean byEvent);

    void toggleRiding(boolean byEvent);

    void toggleHat();
}
