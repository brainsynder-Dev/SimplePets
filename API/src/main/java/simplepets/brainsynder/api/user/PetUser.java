package simplepets.brainsynder.api.user;

import lib.brainsynder.nbt.StorageTagCompound;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.pet.PetType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PetUser {

    /**
     * Returns the UUID of the player.
     */
    UUID getOwnerUUID ();

    /**
     * Returns the name of the player.
     */
    String getOwnerName();

    /**
     * Player instance
     */
    Player getPlayer ();

    /**
     * Will fetch the players location
     *      If player is offline will return empty
     */
    default Optional<Location> getUserLocation () {
        if (getPlayer() instanceof Player) {
            return Optional.of(getPlayer().getLocation());
        }
        return Optional.empty();
    }

    /**
     * Will check if the player already has the compound saved
     *
     * @param compound - The compound being checked
     */
    boolean hasPetSave (StorageTagCompound compound);

    /**
     * Will remove the compound from the players saved pets list
     *
     * @param compound - The compound being removed
     */
    void removePetSave (StorageTagCompound compound);

    /**
     * Will save the entities data to the players saved pets list
     *
     * @param entity - The Entity being saved
     */
    default void addPetSave (IEntityPet entity) {
        if (entity == null) return;
        addPetSave(entity.asCompound());
    }

    /**
     * Whether the user is able to save more pets.
     *
     * If pet-saves is disabled, it will return false.
     */
    boolean canSaveMorePets ();

    boolean summonCachedPets ();

    /**
     * - Will save all the pets currently spawned.
     * - Then it will cache the pets to the server (and database if need be)
     * - Then remove the pets
     */
    void cacheAndRemove ();

    /**
     * Will save the compound to the players saved pets list
     *
     * @param compound - The compound being saved
     */
    void addPetSave (StorageTagCompound compound);

    /**
     * Will fetch the list of pets the player has saved
     */
    List<Entry<PetType, StorageTagCompound>> getSavedPets ();

    /**
     * List of pets the player owns.
     * must be purchased via the plugins economy link
     */
    List<PetType> getOwnedPets ();

    /**
     * Adds the selected {@link PetType} to the players OwnedPets list
     */
    void addOwnedPet (PetType type);

    /**
     * Removes the selected {@link PetType} from the players OwnedPets list
     */
    void removeOwnedPet (PetType type);

    /**
     * Will fetch what name the player gave the {@link PetType}
     *
     * @param type - Type of pet to get the name for
     */
    Optional<String> getPetName (PetType type);

    /**
     * Sets the player pets name
     *     IT WILL OVERRIDE WHAT THE PLAYER HAS CUSTOMIZED
     *
     * @param name - name of the pet
     */
    void setPetName (String name, PetType type);

    /**
     * Contains all the pets that the player has on their heads
     */
    List<PetType> getHatPets();

    /**
     * Does the player any pets spawned
     */
    boolean hasPets ();

    /**
     * Will check if the player has the selected {@link PetType} spawned
     *
     * @param type - The type of pet being checked
     */
    boolean hasPet (PetType type);

    /**
     * Will remove the selected {@link PetType}
     *
     * @param type - The type of pet being removed
     * @return
     *      - true - if the {@link PetType} was removed
     *      - false - if they didn't have the pet spawned
     */
    boolean removePet (PetType type);

    /**
     * Will remove all the players pets
     *
     * @return
     *      - true - if pets were removed
     *      - false - if no pets were removes
     */
    boolean removePets ();

    /**
     * Will return all the players currently spawned pets
     */
    Collection<IEntityPet> getPetEntities ();

    /**
     * Will return the users pet
     */
    Optional<IEntityPet> getPetEntity (PetType type);

    /**
     * Sets the users current pet
     *
     * @param entity
     */
    void setPet (IEntityPet entity);

    /**
     * Is there a pet currently on their owners head
     */
    boolean hasPetHat();

    /**
     * Checks if the type is on the players head
     *
     * @param type
     * @return
     */
    boolean isPetHat (PetType type);

    /**
     * Sets the pet to be a hat or not
     *
     * @param hat
     */
    void setPetHat(PetType type, boolean hat);

    /**
     * Is the pets owner currently riding the pet
     */
    boolean hasPetVehicle();

    /**
     * Whether the user should be able to spawn more pets.
     */
    boolean canSpawnMorePets();

    /**
     * Checks if the type is the pet that the player is riding
     *
     * @param type
     * @return
     */
    boolean isPetVehicle (PetType type);

    /**
     * Sets the pet to be a vehicle or not
     *
     * @param vehicle
     */
    boolean setPetVehicle(PetType type, boolean vehicle);

    /**
     * Updates the pet data gui the player has open (if it is open)
     */
    void updateDataMenu ();

    /**
     * Updates the Pet Selection GUI (If it is open)
     */
    void updateSelectionMenu ();


    class Entry<K,V> {
        private final K key;
        private final V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
