package simplepets.brainsynder.files;

import lib.brainsynder.files.YamlFile;
import lib.brainsynder.utils.Colorize;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.files.options.MessageOption;

import java.io.File;

public class MessageFile {

    private static YamlFile file;

    public static void init (File folder) {
        SimplePets.getDebugLogger().debug(DebugLevel.HIDDEN, "Initializing Messages file");
        file = new YamlFile(folder, "messages.yml") {
            @Override
            public void loadDefaults() {
                addSectionHeader(MessageOption.PREFIX.getPath(), "NOTICE: All the messages in this file can be customized with color codes\nThat includes the HEX color codes added in 1.16\nExample HEX color: &#ff0000 = RED");
                addDefault(MessageOption.PREFIX, "Will replace the {prefix} placeholder");
                addDefault(MessageOption.NO_PERMISSION, "Message will be sent when the player does not have permission for when a permission is required");
                addDefault(MessageOption.NO_PETS_UNLOCKED, "This message will only be used if the player does not have any pets unlocked and 'Needs-Pet-Permission-for-GUI' is TRUE");
                addDefault(MessageOption.SUMMONED_ALL_PETS, "Message that will be sent when pets are spawned via '/pet summon all' (Mostly for OPs to show off)");
                addDefault(MessageOption.SUMMONED_PET, "Message that will be sent when a pet is spawned via '/pet summon'");
                addDefault(MessageOption.MISSING_PET_TYPE, "The pet type is missing");
                addDefault(MessageOption.INVALID_PET_TYPE, "The input is not a valid pet (spelling?)");
                addDefault(MessageOption.PET_IN_DEVELOPMENT, "The selected pet is in development for your version of SimplePets\nAKA we are still working on it expect issues");
                addDefault(MessageOption.PET_NOT_REGISTERED, "The selected pet is not supported for the servers version\n(Or is missing in the jar file [in case it is modified])");
                addDefault(MessageOption.INVALID_NBT, "Message that will show before the 'nbt error message'");
                addDefault(MessageOption.INVALID_NBT_MESSAGE, "Message that will be sent when a pet is spawned via '/pet summon'");
                addDefault(MessageOption.CANT_SPAWN_MORE_PETS, "Message that will be sent if a player attempts to spawn more pets than they are allowed.");
                addDefault(MessageOption.FAILED_SUMMON, "Message that will be sent when a pet is spawned via '/pet summon'");
                addDefault(MessageOption.PLAYER_NOT_ONLINE, "Message that will be sent when a pet is spawned via '/pet summon'");
                addDefault(MessageOption.REMOVED_PET, "Message that will be sent when a pet is spawned via '/pet summon'");
                addDefault(MessageOption.REMOVED_ALL_PETS, "Message that will be sent when a pet is spawned via '/pet summon'");
                addDefault(MessageOption.REMOVED_NOT_SPAWNED, "Message that will be sent when a pet is spawned via '/pet summon'");
                addDefault(MessageOption.MODIFY_COMPOUND, "Message contains what the player has set the pets data to\nSet this as an empty string \"\" to prevent it from being sent");
                addDefault(MessageOption.MODIFY_APPLIED, "Message that will be sent when the compound is applied to the entity");

                addDefault(MessageOption.PET_SAVES_LIMIT_REACHED, "Message that will be sent when the player has reached their global limit for any pet type");
                addDefault(MessageOption.PET_SAVES_LIMIT_REACHED_TYPE, "Message that will be sent when the player has reached their per-pet-type limit");

                addDefault(MessageOption.PURCHASE_ADD, "Message that will be sent when a pet is added to the players purchased list (via '/pets purchased add')");
                addDefault(MessageOption.PURCHASE_REMOVE, "Message that will be sent when a pet is removed from the players purchased list (via '/pets purchased remove')");
                addDefault(MessageOption.PURCHASE_LIST_PREFIX, "Is what is sent before the pets are listed (via '/pets purchased list')");

                addDefault(MessageOption.RENAME_VIA_CHAT, "Message that will be sent when the player is renaming the pet via chat");
                addDefault(MessageOption.RENAME_VIA_CHAT_CANCEL, "Message that will be sent when the player canceled renaming the pet");
                addDefault(MessageOption.RENAME_ANVIL_TITLE, "The title for the pet rename Anvil GUI");
                addDefault(MessageOption.RENAME_ANVIL_TAG, "The name for the NAME_TAG in the Anvil GUI");
                addDefault(MessageOption.RENAME_SIGN_TEXT, "The text that will be set for the sign\n" +
                        "  - One line MUST have {input} to mark what line the player types the pets name\n" +
                        "  - Hex colors can NOT be used for this\n" +
                        "  - MUST have 4 lines");

                addDefault(MessageOption.PET_FILES_REGEN, "Message will be sent when the pets folder has been reset");
                addDefault(MessageOption.INV_FILES_REGEN, "Message will be sent when the inventories folder has been reset");
                addDefault(MessageOption.ITEM_FILES_REGEN, "Message will be sent when the items folder has been reset");
                addDefault(MessageOption.PARTICLE_FILES_REGEN, "Message will be sent when the particles folder has been reset");
                addDefault(MessageOption.PET_TYPE_FILE_REGEN, "Message will be sent when the selected pet file has been reset");

                addDefault(MessageOption.CONFIG_RELOADED, "Message that will be sent when the main config has been reloaded.");
                addDefault(MessageOption.MESSAGES_RELOADED, "Message that will be sent when the messages config has been reloaded.");
                addDefault(MessageOption.INVENTORIES_RELOADED, "Message that will be sent when the inventories manager has been reloaded.");
                addDefault(MessageOption.PARTICLES_RELOADED, "Message that will be sent when the particles manager has been reloaded.");
                addDefault(MessageOption.PETS_RELOADED, "Message that will be sent when the pets manager has been reloaded.");
                addDefault(MessageOption.ALL_RELOADED, "Message that will be sent when all plugin elements have been reloaded.");

                addDefault(MessageOption.CONFIG_UNKNOWN_KEY, "The key entered is not in the selected pets json file");
                addDefault(MessageOption.CONFIG_INVALID_BOOLEAN, "The value entered is not a boolean (aka it is not true or false)");
                addDefault(MessageOption.CONFIG_INVALID_INT, "The value entered is not a valid integer (1,2,3)");
                addDefault(MessageOption.CONFIG_INVALID_DOUBLE, "The value entered is not a valid double (0.1, 0.02, 0.003)");
                addDefault(MessageOption.CONFIG_UNABLE_TO_UPDATE, "The key entered is not able to be updated via the command (probably is an array or an object)");
                addDefault(MessageOption.CONFIG_VALUE_UPDATED, """
                        The key has been updated with the new value
                        
                        Placeholders:
                        {key} - The target key
                        {value} - The new value
                        {type} - The type of pet selected
                        """);
                addDefault(MessageOption.CONFIG_VALUE_RESET, """
                        The key has been reset with the default value
                        
                        Placeholders:
                        {key} - The target key
                        {value} - The new value
                        {type} - The type of pet selected
                        """);
            }
        };
    }

    public static YamlFile getFile() {
        return file;
    }

    public static String getTranslation (MessageOption option) {
        String message = file.getString(option);
        if (message.contains("{prefix}") && (option != MessageOption.PREFIX)) message = message.replace("{prefix}", file.getString(MessageOption.PREFIX));
        return Colorize.translateBungeeHex(message);
    }


}
