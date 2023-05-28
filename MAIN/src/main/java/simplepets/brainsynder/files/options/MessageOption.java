package simplepets.brainsynder.files.options;

import com.google.common.collect.Lists;
import lib.brainsynder.files.options.YamlOption;

import java.util.Arrays;
import java.util.List;

public enum MessageOption implements YamlOption {
    PREFIX ("prefix", "&eSimplePets &6>>"),
    NO_PERMISSION ("no_permission", "{prefix} &cYou do not have permission."),
    NO_PETS_UNLOCKED ("no_pets_unlocked", "{prefix} &cYou do not have any pets unlocked."),
    SUMMONED_ALL_PETS ("summon.all_pets", "{prefix} &7Successfully summoned {count} pets!"),
    SUMMONED_PET ("summon.pet", "{prefix} &7Successfully summoned the {type} pet!"),

    PURCHASE_ADD("purchased.added", "{prefix} &7{type} was added to the purchased pets of &c{player}"),
    PURCHASE_REMOVE ("purchased.removed", "{prefix} &7{type} was removed from the purchased pets of &c{player}"),
    PURCHASE_LIST_PREFIX ("purchased.list_prefix", "{prefix} &7Owned Pets: "),

    MISSING_PET_TYPE ("pet_type.missing", "{prefix} &cMissing pet type."),
    INVALID_PET_TYPE ("pet_type.invalid", "{prefix} &cSorry, &7'{arg}' &cis not a valid pet type."),
    PET_NOT_REGISTERED ("pet_type.not_registered", "{prefix} &cSorry, {type} is not registered."),
    PET_NOT_REGISTERED_LORE ("pet_type.not_registered_lore", "&cNOT REGISTERED"),
    PET_IN_DEVELOPMENT ("pet_type.in_development", "{prefix} &cSorry, {type} is currently in-development and not able to be used."),
    PET_NOT_SUPPORTED ("pet_type.not_supported", "{prefix} &cSorry, {type} is not supported for this version."),
    PET_NOT_SUPPORTED_LORE ("pet_type.not_supported_lore", "&cNOT SUPPORTED"),

    INVALID_NBT ("nbt.invalid", "{prefix} &cInvalid nbt has been entered."),
    INVALID_NBT_MESSAGE ("nbt.error", "{prefix} &c{message}"),
    FAILED_SUMMON ("summon.failed", "{prefix} &cSorry, the {type} pet was unable to be spawned at the moment."),
    CANT_SPAWN_MORE_PETS("summon.cant_spawn_more_pets", "{prefix} &cYou can't spawn any more pets!"),
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

    PET_SAVES_LIMIT_REACHED ("pet-saves.limit-reached", "{prefix} &cYou have reached your limit for saving pet"),
    PET_SAVES_LIMIT_REACHED_TYPE("pet-saves.limit-reached-per-type", "{prefix} &cYou have reached your limit for saving {type} pets"),

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
    ALL_RELOADED("admin.reload.all", "{prefix} &7All plugin elements have been reloaded!"),

    CONFIG_UNKNOWN_KEY("admin.pet-config.unknown-key", "{prefix} &7{key} &cis not a key for the pets json file."),
    CONFIG_INVALID_BOOLEAN("admin.pet-config.invalid-boolean", "{prefix} &7{value} &cis not a valid boolean, please use true/false"),
    CONFIG_INVALID_INT("admin.pet-config.invalid-integer", "{prefix} &7{value} &cis not a valid integer."),
    CONFIG_INVALID_DOUBLE("admin.pet-config.invalid-double", "{prefix} &7{value} &cis not a valid double."),
    CONFIG_UNABLE_TO_UPDATE("admin.pet-config.unable-to-update", "{prefix} &cUnable to update this key."),
    CONFIG_VALUE_UPDATED("admin.pet-config.value-set", "{prefix} &a{key} &7has been set to &e{value}"),
    CONFIG_VALUE_RESET("admin.pet-config.value-reset", "{prefix} &a{key} &7has been reset to the default value");

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
