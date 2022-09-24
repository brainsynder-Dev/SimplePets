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
    /**
     * Teleports the pet to its owners location
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
     * Returns the UUID of the pets owner.
     *
     * @return The owner's UUID.
     */
    UUID getOwnerUUID();

    /**
     * Get the pet user.
     *
     * @return The PetUser object.
     */
    PetUser getPetUser();

    /**
     * Returns the pet type.
     *
     * @return The PetType object.
     */
    PetType getPetType();

    /**
     * Returns the visible entity for the pet
     *      EG: Shulker pet will return the shulker entity not the ghost armor stand its riding
     */
    Entity getEntity();

    /**
     * This function is called when the player starts riding the pet
     * It handles how the player mounts, For instance when riding horse pets
     * the plugin will add a SeatEntity as a controller for the pet
     *
     * See this GitHub Pull Request for more info
     * <a href="https://github.com/brainsynder-Dev/SimplePets/pull/114">#114</a>
     *
     * @return A boolean value.
     */
    boolean attachOwner();

    /**
     * Returns a list of entities that are involved in the pet.
     *
     * @return A list containing the entities.
     */
    default List<Entity> getEntities() {
        return Collections.singletonList(getEntity());
    }

    /**
     * Returns a StorageTagCompound object that represents the pets data and features.
     *
     * @return A StorageTagCompound object.
     */
    StorageTagCompound asCompound();

    /**
     * It applies the given compound to the pet
     *
     * @param object The object to apply the compound to.
     */
    void applyCompound(StorageTagCompound object);

    default boolean isBig() {
        return false;
    }

    /**
     * Returns true if the pet is silent, false otherwise.
     * Will the pet make its ambient sounds?
     *
     * @return false
     */
    default boolean isPetSilent() {
        return false;
    }

    /**
     * Sets whether the pet is silent or not.
     *
     * @param silent If true, the pet will not make any sounds.
     */
    default void setPetSilent(boolean silent) {}

    /**
     * Will fetch the pets name
     *  - If the name was previously modified via {@link IEntityPet#setPetName(String)} it will return that name
     *  - If the player has a default name for it
     *  - empty if there is no name
     *
     * @return Optional<String>
     */
    Optional<String> getPetName ();

    /**
     * This function sets the pet's name.
     *
     * @param name The name of the pet.
     */
    void setPetName (String name);

    /**
     * Returns true if the pet is frozen (shaking)
     *
     * @since MC 1.17
     * @return false
     */
    default boolean isFrozen () {
        return false;
    }

    /**
     * This will toggle weather the pet is fully frozen or not
     *
     * @since MC 1.17
     * @param frozen
     */
    default void setFrozen (boolean frozen) {}

    /**
     * Returns true if the pet is visible.
     *
     * @return A boolean value.
     */
    boolean isPetVisible ();

    /**
     * Sets the visibility of the pet
     *
     * @param visible true to make the pet visible, false to hide it.
     */
    void setPetVisible (boolean visible);

    /**
     * Sets the color of the glow effect on the pet.
     *
     * @param color The color of the glow.
     */
    @Deprecated void setGlowColor (ChatColor color);

    /**
     * Returns the color of the glow effect
     *
     * @return The glow color of the pet.
     */
    ChatColor getGlowColor ();
}
