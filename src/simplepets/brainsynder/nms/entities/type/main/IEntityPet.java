package simplepets.brainsynder.nms.entities.type.main;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.pet.IPet;

public interface IEntityPet extends IImpossaPet {
    Player getOwner();

    IPet getPet();

    Entity getEntity();

    StorageTagCompound asCompound();

    void applyCompound(StorageTagCompound object);

    default boolean isBig() {
        return false;
    }

    void setPassenger(int pos, org.bukkit.entity.Entity entity, org.bukkit.entity.Entity passenger);

    void removePassenger(org.bukkit.entity.Entity entity);
}
