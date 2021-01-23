package api.entity;

import lib.brainsynder.nbt.StorageTagCompound;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import simplepets.brainsynder.api.entity.misc.IEntityBase;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public interface IEntityPet extends IEntityBase {
    void teleportToOwner ();

    /**
     * Used for other plugins to use as storage for the pet
     *
     * @param pluginKey - A unique key for the plugin/addon (so the data does not get merged unnecessarily)
     * @param compound - Get/Set data that is contained within the Additional Storage
     */
    void handleAdditionalStorage (String pluginKey, Function<StorageTagCompound, StorageTagCompound> compound);

    UUID getOwnerUUID();

    PetUser getPetUser();

    PetType getPetType();

    Entity getEntity();

    default List<Entity> getEntities() {
        return Arrays.asList(getEntity());
    }

    StorageTagCompound asCompound();

    void applyCompound(StorageTagCompound object);

    default boolean isBig() {
        return false;
    }

    default boolean isPetSilent() {
        return false;
    }

    default void setPetSilent(boolean silent) {}

    // This will simply make it so when the pet get changed
    // The location will stay the same and it wont keep walking.
    void setWalkToLocation (Location location);
    Location getWalkToLocation ();

    Optional<String> getPetName ();

    /**
     * Sets the player pets name
     * @param name - name of the pet
     */
    void setPetName (String name);
}
