package simplepets.brainsynder.api.plugin.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.pet.CommandReason;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

public interface IPetUtilities {
    /**
     * "Runs the commands for the pet of the given type for the given owner."
     * <p>
     * The first parameter is the reason for the commands to be run. This is an enum that can be one of the following:
     *
     * <ul>
     *     <li><b><u>CommandReason.SPAWN</u></b>  -  <i>The pet has just been successfully spawned</i></li>
     *     <li><b><u>CommandReason.REVOKE</u></b>  -  <i>The pet was removed</i></li>
     *     <li><b><u>CommandReason.RIDE</u></b>  -  <i>The player just started riding their pet</i></li>
     *     <li><b><u>CommandReason.RIDE_DISMOUNT</u></b>  -  <i>The player has stopped riding their pet</i></li>
     *     <li><b><u>CommandReason.HAT</u></b>  -  <i>The pet was just toggled as a hat</i></li>
     *     <li><b><u>CommandReason.TELEPORT</u></b>  -  <i>The pet was just teleported to the player</i></li>
     *     <li><b><u>CommandReason.FAILED</u></b>  -  <i>A task failed to run for the pet (Could be failed spawn or something along those lines)</i></li>
     * </ul>
     *
     * @param reason The reason the command was run.
     * @param owner  The player who owns the pet.
     * @param type   The type of pet.
     */
    void runPetCommands(CommandReason reason, PetUser owner, PetType type);

    /**
     * "Runs the commands for the given pet type, for the given reason, at the given location."
     * <p>
     * The first parameter, `reason`, is the reason for the commands to be run. This can be one of the following:
     *
     * <ul>
     *     <li><b><u>CommandReason.SPAWN</u></b>  -  <i>The pet has just been successfully spawned</i></li>
     *     <li><b><u>CommandReason.REVOKE</u></b>  -  <i>The pet was removed</i></li>
     *     <li><b><u>CommandReason.RIDE</u></b>  -  <i>The player just started riding their pet</i></li>
     *     <li><b><u>CommandReason.RIDE_DISMOUNT</u></b>  -  <i>The player has stopped riding their pet</i></li>
     *     <li><b><u>CommandReason.HAT</u></b>  -  <i>The pet was just toggled as a hat</i></li>
     *     <li><b><u>CommandReason.TELEPORT</u></b>  -  <i>The pet was just teleported to the player</i></li>
     *     <li><b><u>CommandReason.FAILED</u></b>  -  <i>A task failed to run for the pet (Could be failed spawn or something along those lines)</i></li>
     * </ul>
     *
     * @param reason   The reason the command was run.
     * @param owner    The player who owns the pet.
     * @param type     The type of pet you want to spawn.
     * @param location The location of the pet.
     */
    void runPetCommands(CommandReason reason, PetUser owner, PetType type, Location location);

    /**
     * "This function is called when a pet's name is being displayed to a player. It allows you to replace placeholders in
     * the pet's name with custom text."
     * <p>
     * The first parameter is the owner of the pet. The second parameter is the pet itself. The third parameter is the
     * location of the pet. The fourth parameter is the text that is being displayed
     *
     * @param owner  The owner of the pet
     * @param entity The entity that the pet is.
     * @param petLoc The location of the pet.
     * @param text   The text to be parsed
     * @return A string with the placeholders replaced.
     */
    String handlePlaceholders(PetUser owner, IEntityPet entity, Location petLoc, String text);

    /**
     * Given a pet name, return the name of the pet with any of the chat colors/effects the player has access to
     *
     * @param name The name of the pet.
     * @return A string.
     */
    String translatePetName(String name);

    /**
     * If the player has a metadata value of "vanished" that is true, then return true
     * <p>
     * This is used in some vanish plugins like:
     * <ul>
     *     <li><a href="https://www.spigotmc.org/resources/14404/">PremiumVanish</a></li>
     *     <li><a href="https://www.spigotmc.org/resources/1331/">SuperVanish</a></li>
     *     <li><a href="https://dev.bukkit.org/projects/vanish/">VanishNoPacket</a></li>
     * </ul>
     *
     * @param player The player to check if they are vanished.
     * @return A boolean value.
     */
    default boolean isVanished(Player player) {
        if (player == null) return false;
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }
}
