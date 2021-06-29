package simplepets.brainsynder.api.user;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface UserManagement {
    /**
     * Fetches the {@link PetUser} from the players username
     *
     * @param username - username of the player
     * @param consumer - what happens after it fetches the {@link PetUser}
     */
    void getPetUser(String username, Consumer<Optional<PetUser>> consumer);

    /**
     * Fetches the {@link PetUser} from the players {@link UUID}
     *
     * @param uuid - Players UUID
     */
    Optional<PetUser> getPetUser(UUID uuid);

    /**
     * Fetches the {@link PetUser} of the {@link Player}
     *
     * @param player - player instance
     */
    Optional<PetUser> getPetUser(Player player);

    Collection<PetUser> getAllUsers ();

    boolean isUserCached(Player player);
}
