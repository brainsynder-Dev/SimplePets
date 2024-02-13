package simplepets.brainsynder.api.entity;

import lib.brainsynder.nbt.StorageTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import simplepets.brainsynder.api.entity.misc.IBurnablePet;
import simplepets.brainsynder.api.entity.misc.IEntityBase;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public interface IEntityPet extends IEntityBase, IBurnablePet {
    default void togglePetHatTask (boolean value) {}

    /**
     * Will teleport the pet to its owner
     */
    void teleportToOwner ();

    /**
     * Used for other plugins to use as storage for the pet
     *
     * @param pluginKey - A unique key for the plugin/addon (so the data does not get merged unnecessarily)
     * @param compound - Get/Set data that is contained within the Additional Storage
     */
    void handleAdditionalStorage (String pluginKey, Function<StorageTagCompound, StorageTagCompound> compound);

    /**
     * UUID of the pets owner
     */
    UUID getOwnerUUID();

    PetUser getPetUser();

    PetType getPetType();

    /**
     * Returns the visible entity for the pet
     *      EG: Shulker pet will return the shulker entity not the ghost armor stand its riding
     */
    Entity getEntity();

    boolean attachOwner();

    /**
     * Will return all the entities involved in the pet
     */
    default List<Entity> getEntities() {
        return Collections.singletonList(getEntity());
    }

    /**
     * Will save all the pets current data to a STC
     */
    StorageTagCompound asCompound();

    /**
     * Will apply data to the pet EG: if the pet is a baby
     * @param object - The data to modify the pet with
     */
    void applyCompound(StorageTagCompound object);

    default boolean isBig() {
        return false;
    }

    /**
     * Will the pet make its ambient sounds?
     */
    default boolean isPetSilent() {
        return false;
    }

    default void setPetSilent(boolean silent) {}

    /**
     * Will fetch the pets name
     *  - If the name was previously modified via {@link IEntityPet#setPetName(String)} it will return that name
     *  - If the player has a default name for it
     *  - empty if there is no name
     */
    Optional<String> getPetName ();

    /**
     * Sets the player pets name
     * @param name - name of the pet
     */
    void setPetName (String name);

    /**
     * Added in 1.17
     *
     * This checks if the pet is frozen, if it is it will shake
     */
    default boolean isFrozen () {
        return false;
    }

    /**
     * Added in 1.17
     *
     * This will toggle weather the pet is fully frozen or not
     *
     * @param frozen
     */
    default void setFrozen (boolean frozen) {}

    /**
     * Is the pet visible to players
     */
    boolean isPetVisible ();

    /**
     * This will toggle weather the pet is visible to players or not
     *
     * @param visible
     */
    void setPetVisible (boolean visible);

    /**
     * This will set the color the pet will glow as
     *
     * @param color
     */
    void setGlowColor (ChatColor color);

    /**
     * Returns what color the pet will glow as
     */
    ChatColor getGlowColor ();
}
