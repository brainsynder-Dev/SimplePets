package simplepets.brainsynder.managers;

import lib.brainsynder.web.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.user.UserManagement;
import simplepets.brainsynder.impl.PetOwner;

import java.util.*;
import java.util.function.Consumer;

public class UserManager implements UserManagement {
    private final Map<UUID, PetUser> userMap;
    private final Map<String, UUID> uuidCache;
    private final PetCore PLUGIN;

    public UserManager(PetCore plugin) {
        this.PLUGIN = plugin;
        this.userMap = new HashMap<>();
        this.uuidCache = new HashMap<>();
    }

    @Override
    public void getPetUser(String username, Consumer<Optional<PetUser>> consumer) {
        if (uuidCache.containsKey(username)) {
            consumer.accept(getPetUser(uuidCache.get(username)));
            return;
        }

        // Searches for the users profile data
        PlayerData.findProfile(username, PLUGIN, members -> {

            // fetches the UUID from the JSON
            String raw = members.getString("uuid_formatted", null);

            // If the UUID is null it means the site could not find the information
            if (raw == null) {
                consumer.accept(Optional.empty());
                return;
            }

            UUID uuid = UUID.fromString(raw);

            // Cache the result only if a valid UUID is found
            uuidCache.put(username, uuid);

            consumer.accept(getPetUser(uuid));
        }, throwable -> consumer.accept(Optional.empty()));
    }

    @Override
    public Optional<PetUser> getPetUser(UUID uuid) {
        return getPetUser(Bukkit.getPlayer(uuid));
    }

    @Override
    public Optional<PetUser> getPetUser(Player player) {
        if (player == null) return Optional.empty();

        PetUser user;
        if (userMap.containsKey(player.getUniqueId())) {
            user = userMap.get(player.getUniqueId());
        }else{
            user = new PetOwner(player);
            userMap.put(player.getUniqueId(), user);
        }
        return Optional.of(user);
    }


    @Override
    public Collection<PetUser> getAllUsers() {
        return userMap.values();
    }

    @Override
    public boolean isUserCached(Player player) {
        return userMap.containsKey(player.getUniqueId());
    }


}
