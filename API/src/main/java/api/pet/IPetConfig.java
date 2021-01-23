package api.pet;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.function.Function;

public interface IPetConfig {
    void handleAdditionalStorage (String pluginKey, Function<JsonObject, JsonObject> json);

    /**
     * The configured display name that a pet will be named
     * <p>
     * Example: Steve's Cow Pet
     */
    String getDisplayName();

    /**
     * Will check if a player has permission for a pet (Will also follow if NeedsPermission is toggled)
     *
     * @param player
     */
    boolean hasPermission(Player player);

    /**
     * Will check if the player is allowed to have the pet as a hat
     *
     * @param player
     */
    boolean canHat(Player player);

    /**
     * Will check if the player can ride the pet
     *
     * @param player
     */
    boolean canMount(Player player);

    /**
     * Will check if the player is allowed to fly with the pet
     *
     * @param player
     */
    boolean canFly(Player player);

    /**
     * Will check if the pet is enabled
     */
    boolean isEnabled();

    /**
     * Is the pet able to float down when falling (slower falling)
     */
    boolean canFloat();

    /**
     * How fast should the pet be able to move when riding
     * <p>
     * Example: 0.2500000138418579
     */
    double getRideSpeed();

    /**
     * How fast should the pet be able to move when following the player
     * <p>
     * Example: 0.6000000138418579
     */
    double getWalkSpeed();

    /**
     * What type of entity is the pet
     */
    Optional<EntityType> getEntityType();

    /**
     * What is the Ambient sound the pet makes
     */
    SoundMaker getSound();
}
