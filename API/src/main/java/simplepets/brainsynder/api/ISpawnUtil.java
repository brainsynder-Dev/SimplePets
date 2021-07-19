package simplepets.brainsynder.api;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.optional.BiOptional;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

import java.util.Map;
import java.util.Optional;

public interface ISpawnUtil {

    /**
     * Will spawn the selected pet
     *
     * @param type - The type of pet to be summoned
     * @param user - User the pet is spawned for
     */
    BiOptional<IEntityPet, String> spawnEntityPet(PetType type, PetUser user);

    /**
     * Will spawn the selected pet
     *
     * @param type - The type of pet to be summoned
     * @param user - User the pet is spawned for
     * @param compound - Customization for the entity
     */
    BiOptional<IEntityPet, String> spawnEntityPet(PetType type, PetUser user, StorageTagCompound compound);

    /**
     * Will spawn the selected pet
     *
     * @param type - The type of pet to be summoned
     * @param user - User the pet is spawned for
     * @param location - Location to spawn the pet at
     */
    BiOptional<IEntityPet, String> spawnEntityPet(PetType type, PetUser user, Location location);

    /**
     * Will spawn the selected pet
     *
     * @param type - The type of pet to be summoned
     * @param user - User the pet is spawned for
     * @param compound - Customization for the entity
     * @param location - Location to spawn the pet at
     */
    BiOptional<IEntityPet, String> spawnEntityPet(PetType type, PetUser user, StorageTagCompound compound, Location location);

    /**
     * Will check if the {@link PetType} is registered
     * @param type - Type being check
     */
    boolean isRegistered (PetType type);

    /**
     * Fetches the NMS version of the entity
     *
     * @param entity - entity to be fetched from
     */
    Optional<Object> getHandle(Entity entity);

    /**
     * This method simply is a way to track how many times a pet was successfully spawned
     */
    Map<PetType, Integer> getSpawnCount ();
}
