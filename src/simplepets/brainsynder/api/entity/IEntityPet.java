package simplepets.brainsynder.api.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.pet.IPet;

public interface IEntityPet extends IImpossaPet {
    Player getOwner();

    IPet getPet();

    Entity getEntity();

    StorageTagCompound asCompound();

    void applyCompound(StorageTagCompound object);

    default boolean isBig() {
        return false;
    }
}
