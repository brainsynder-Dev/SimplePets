package api.user;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.pet.PetType;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PetUser {
    /**
     * Player instance
     */
    OfflinePlayer getPlayer ();

    default Optional<Location> getUserLocation () {
        if (getPlayer() instanceof Player) {
            return Optional.of(((Player)getPlayer()).getLocation());
        }
        return Optional.empty();
    }

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
     *     IT WILL OVERRIDE WHAT THE PLAYER CUSTOMIZED
     *
     * @param name - name of the pet
     */
    void setPetName (String name, PetType type);

    /**
     * Sets the player pets name
     *     IT WILL OVERRIDE WHAT THE PLAYER CUSTOMIZED
     *
     * @param name - name of the pet
     * @param override - Should it run all the checks (player perms) or just override the name
     */
    void setPetName (String name, PetType type, boolean override);

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
     * Is the pet currently on their owners head
     */
    boolean isPetHat();

    /**
     * Sets the pet to be a hat or not
     *
     * @param hat
     */
    void setPetHat(boolean hat);

    /**
     * Is the pets owner currently riding the pet
     */
    boolean isPetVehicle();

    /**
     * Sets the pet to be a vehicle or not
     *
     * @param vehicle
     * @param byEvent - Was this method triggered by an event
     */
    boolean setPetVehicle(boolean vehicle, boolean byEvent);

    /**
     * Updates the DataMenu GUI
     *      Will only update the menu they currently have open
     */
    void updateDataMenu ();
}
