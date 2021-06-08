package simplepets.brainsynder.files.options;

import com.google.common.collect.Lists;
import lib.brainsynder.files.options.YamlOption;

import java.util.Arrays;
import java.util.List;

public enum MessageOption implements YamlOption {
    PREFIX ("prefix", "&eSimplePets &6>>"),
    NO_PERMISSION ("no_permission", "{prefix} &cYou do not have permission."),
    SUMMONED_ALL_PETS ("summon.all_pets", "{prefix} &7Successfully summoned {count} pets!"),
    SUMMONED_PET ("summon.pet", "{prefix} &7Successfully summoned the {type} pet!"),

    PURCHASE_ADD("purchased.added", "{prefix} &7{type} was added to the purchased pets of &c{player}"),
    PURCHASE_REMOVE ("purchased.removed", "{prefix} &7{type} was removed from the purchased pets of &c{player}"),
    PURCHASE_LIST_PREFIX ("purchased.list_prefix", "{prefix} &7Owned Pets: "),

    MISSING_PET_TYPE ("pet_type.missing", "{prefix} &cMissing pet type."),
    INVALID_PET_TYPE ("pet_type.invalid", "{prefix} &cSorry, &7'{arg}' &cis not a valid pet type."),
    PET_NOT_REGISTERED ("pet_type.not_registered", "{prefix} &cSorry, {type} is not registered."),
    PET_NOT_REGISTERED_LORE ("pet_type.not_registered_lore", "&cNOT REGISTERED"),
    PET_NOT_SUPPORTED ("pet_type.not_supported", "{prefix} &cSorry, {type} is not supported for this version."),
    PET_NOT_SUPPORTED_LORE ("pet_type.not_supported_lore", "&cNOT SUPPORTED"),

    INVALID_NBT ("nbt.invalid", "{prefix} &cInvalid nbt has been entered."),
    INVALID_NBT_MESSAGE ("nbt.error", "{prefix} &c{message}"),
    FAILED_SUMMON ("summon.failed", "{prefix} &cSorry, the {type} pet was unable to be spawned at the moment."),
    PLAYER_NOT_ONLINE ("player_not_online", "{prefix} &c{player} is not online (spelling?)"),
    REMOVED_PET ("remove.removed_pet", "{prefix} &7Successfully removed the {type} pet!"),
    REMOVED_ALL_PETS ("remove.all_pets", "{prefix} &7Successfully removed {count} pets!"),
    REMOVED_NOT_SPAWNED ("remove.not_spawned", "{prefix} &cSorry, {type} is not spawned."),
    MODIFY_COMPOUND ("modify.compound", "{prefix} &7NBT compound: &e{compound}"),
    MODIFY_APPLIED ("modify.applied", "{prefix} &7Data has been applied to the {type} pet!"),
    RENAME_VIA_CHAT ("rename.via_chat", "{prefix} &7Type your pets new name in chat:"),
    RENAME_VIA_CHAT_CANCEL ("rename.cancel", "{prefix} &cPet renaming has been canceled"),
    RENAME_ANVIL_TITLE ("rename.anvil.title", "&#de9790[] &#b35349Rename Pet"),
    RENAME_ANVIL_TAG ("rename.anvil.tag_name", "&#de9790NEW NAME"),
    RENAME_SIGN_TEXT ("rename.sign.lines", Lists.newArrayList("{input}", "&l^^^^^^^^", "&9&lPlease Enter", "&9&lPet Name")),

    PET_FILES_REGEN ("admin.regenerate.pets", "{prefix} &7The Pets folder has been regenerated to the default files."),
    INV_FILES_REGEN ("admin.regenerate.inventories", "{prefix} &7The Inventories folder has been regenerated to the default files."),
    ITEM_FILES_REGEN ("admin.regenerate.items", "{prefix} &7The Items folder has been regenerated to the default files."),
    PARTICLE_FILES_REGEN ("admin.regenerate.particles", "{prefix} &7The Particles folder has been regenerated to the default files."),
    PET_TYPE_FILE_REGEN ("admin.regenerate.pet_type", "{prefix} &7The file for the {type} pet has been reset to the default file."),

    CONFIG_RELOADED("admin.reload.config", "{prefix} &7The plugin configuration has been reloaded!"),
    MESSAGES_RELOADED("admin.reload.messages", "{prefix} &7The plugin's message configuration has been reloaded!"),
    INVENTORIES_RELOADED("admin.reload.inventories", "{prefix} &7Items and inventories have been reloaded!"),
    PARTICLES_RELOADED("admin.reload.particles", "{prefix} &7Particles have been reloaded!"),
    PETS_RELOADED("admin.reload.pets", "{prefix} &7Pets have been reloaded!"),
    ALL_RELOADED("admin.reload.all", "{prefix} &7All plugin elements have been reloaded!");

    private final String path;
    private final Object defaultValue;
    private final List<String> oldPaths;

    MessageOption(String path, Object defaultValue) {
        this(path, defaultValue, new String[0]);
    }

    MessageOption(String path, Object defaultValue, String... oldPaths) {
        this.path = path;
        this.defaultValue = defaultValue;
        this.oldPaths = Arrays.asList(oldPaths);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Object getDefault() {
        return defaultValue;
    }

    @Override
    public List<String> getOldPaths() {
        return oldPaths;
    }
}
