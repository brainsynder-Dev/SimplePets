package simplepets.brainsynder.files;

import lib.brainsynder.files.YamlFile;
import net.md_5.bungee.api.ChatColor;
import simplepets.brainsynder.files.options.MessageOption;

import java.io.File;

public class MessageFile {

    private static YamlFile file;

    public static void init (File folder) {
        file = new YamlFile(folder, "messages.yml") {
            @Override
            public void loadDefaults() {
                addDefault(MessageOption.PREFIX, "Will replace the {prefix} placeholder");
                addDefault(MessageOption.SUMMONED_ALL_PETS, "Message that will be sent when pets are spawned via '/pet summon all' (Mostly for OPs to show off)");
                addDefault(MessageOption.SUMMONED_PET, "Message that will be sent when a pet is spawned via '/pet summon'");
                addDefault(MessageOption.INVALID_PET_TYPE, "The input is not a valid pet (spelling?)");
                addDefault(MessageOption.PET_NOT_REGISTERED, "The selected pet is not supported for the servers version\n(Or is missing in the jar file [in case it is modified])");
                addDefault(MessageOption.INVALID_NBT, "Message that will show before the 'nbt error message'");
                addDefault(MessageOption.INVALID_NBT_MESSAGE, "Message that will be sent when a pet is spawned via '/pet summon'");
                addDefault(MessageOption.FAILED_SUMMON, "Message that will be sent when a pet is spawned via '/pet summon'");
                addDefault(MessageOption.PLAYER_NOT_ONLINE, "Message that will be sent when a pet is spawned via '/pet summon'");
                addDefault(MessageOption.REMOVED_PET, "Message that will be sent when a pet is spawned via '/pet summon'");
                addDefault(MessageOption.REMOVED_ALL_PETS, "Message that will be sent when a pet is spawned via '/pet summon'");
                addDefault(MessageOption.REMOVED_NOT_SPAWNED, "Message that will be sent when a pet is spawned via '/pet summon'");
                addDefault(MessageOption.MODIFY_COMPOUND, "Message contains what the player has set the pets data to\nSet this as an empty string \"\" to prevent it from being sent");
                addDefault(MessageOption.MODIFY_APPLIED, "Message that will be sent when the compound is applied to the entity");
            }
        };
    }

    public static String getTranslation (MessageOption option) {
        String message = file.getString(option);
        if (message.contains("{prefix}") && (option != MessageOption.PREFIX)) message = message.replace("{prefix}", file.getString(MessageOption.PREFIX));
        return ChatColor.translateAlternateColorCodes('&', message);
    }


}
