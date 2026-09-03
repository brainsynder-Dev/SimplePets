package simplepets.brainsynder.api.plugin.config;

import com.google.common.collect.Lists;
import simplepets.brainsynder.api.plugin.config.internal.ConfigEntry;
import simplepets.brainsynder.api.plugin.config.internal.ConfigRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface ConfigOption {
    ConfigRegistry REGISTRY = new ConfigRegistry();

    ConfigEntry<Boolean> RELOAD_DETECT = REGISTRY.register("Reload-Detected", false,
            """
                    This is used by the plugin to detect if the plugin was previously unloaded (via /reload or by a plugin)
                        CAN NOT BE CUSTOMIZED
                    """);
    ConfigEntry<Boolean> SIMPLER_GUI = REGISTRY.register("Simpler-Pet-GUI-Command", false,
            """
                    UGGGGGGGG This config option makes it so `/pet` opens the GUI (like `/pet gui`)
                    Requires a server restart for some reason ¯\\_(ツ)_/¯

                    Default: {default}""");

    // Update Checking
    ConfigEntry<Boolean> UPDATE_CHECK_ENABLED = REGISTRY.register("Update-Checking.Enabled", true,
            """
                    Would you like to check for when there is a new update?

                    Default: {default}""");

    // Permissions
    ConfigEntry<List<String>> PERMISSIONS_IGNORE_LIST = REGISTRY.register("Permissions.Ignored-List", new ArrayList<>(),
            """
                    Any permission in this list will be ignored when the plugin checks if the player has permission
                    (Will act like the player has the permission, without actually having it)

                    Default: {default}""");
    ConfigEntry<Boolean> PERMISSIONS_ENABLED = REGISTRY.register("Permissions.Enabled", true,
            """
                    Disabling this would grant ALL players access to pets (they wont need permissions)

                    Default: {default}""");
    ConfigEntry<Boolean> PERMISSIONS_OPEN_GUI = REGISTRY.register("Permissions.Needs-Pet-Permission-for-GUI", false,
            """
                    Enabling this would require players to have access to at least 1 pets permission

                    Default: {default}""");
    ConfigEntry<Boolean> PERMISSIONS_DATA_PERMS = REGISTRY.register("Permissions.Data-Permissions", true,
            """
                    Disabling this will make it so players do not need to have any data permissions (EG. pet.type.armorstand.data.silent)

                    Default: {default}""");
    ConfigEntry<Boolean> PERMISSIONS_PLAYER_ACCESS = REGISTRY.register("Permissions.Only-Show-Pets-Player-Can-Access", true,
            """
                    Enabling this would remove all the pets the player does not have access to from the GUI

                    Default: {default}""");


    // Addon Config
    ConfigEntry<String> ADDON_LOAD_UNIT = REGISTRY.register("addon-initialization.unit", TimeUnit.SECONDS.name(),
            """
                    The unit of time for when the addons should be loaded

                    Time Units:
                    - SECONDS
                    - MINUTES
                    - HOURS
                    - DAYS

                    Default: {default}""");
    ConfigEntry<Integer> ADDON_LOAD_TIME = REGISTRY.register("addon-initialization.time", 5,
            """
            The integer value which correlates to the selected unit

            Example:
                unit: HOURS
                time: 12
            Will be 12 Hours till the addons initiate

            Default: {default}
            """);


    ConfigEntry<Boolean> REMOVE_PET_ON_WORLD_CHANGE = REGISTRY.register("RemovePetsOnWorldChange", true,
            """
                    Disabling this will remove a players pet when they change worlds

                    Default: {default}""");


    ConfigEntry<Boolean> RESPAWN_LAST_PET_LOGIN = REGISTRY.register("Respawn-Last-Pet-On-Login", true,
            """
                    When a player logs back in should their pet be spawned in as well?
                    NOTE: If the player removed their pet before logging out then it wont respawn.

                    Default: {default}""");


    ConfigEntry<Boolean> UTILIZE_PURCHASED_PETS = REGISTRY.register("Utilize-Purchased-Pets", false,
            """
                    This option will make it so if the player has the pet (was 'purchased' either via economy or the command)
                    It will allow the player to spawn that pet regardless if they have permission to it

                    Default: {default}""");


    ConfigEntry<Boolean> AUTO_REMOVE_ENABLED = REGISTRY.register("auto-remove.enabled", true,
            """
                    Disabling this will make it so pets wont be automatically removed if the player is afk

                    Default: {default}""").pastPaths("PetToggles.AutoRemove.Enabled");
    ConfigEntry<Integer> AUTO_REMOVE_TICK = REGISTRY.register("auto-remove.tick-delay", 10000,
            """
                    What should the wait be?
                    This is in ticks (20 ticks = 1 second)
                    Example: 10000 ≈ 8 minutes 20 seconds

                    Default: {default}""").pastPaths("PetToggles.AutoRemove.TickDelay");


    // Particle Toggles
    ConfigEntry<Boolean> PARTICLES_SUMMON_TOGGLE = REGISTRY.register("Particles.Summon", true,
            """
                    Disabling this would make it so there is no particles when a player spawns a pet

                    Default: {default}""");
    ConfigEntry<Boolean> PARTICLES_FAILED_TOGGLE = REGISTRY.register("Particles.Failed", true,
            """
                    Disabling this would make it so there is no particles when a task fails for a player (EG: spawning, riding, etc..)

                    Default: {default}""");
    ConfigEntry<Boolean> PARTICLES_REMOVE_TOGGLE = REGISTRY.register("Particles.Remove", true,
            """
                    Disabling this would make it so there is no particles when a player removes a pet

                    Default: {default}""");
    ConfigEntry<Boolean> PARTICLES_RENAME_TOGGLE = REGISTRY.register("Particles.Name-Change", true,
            """
                    Disabling this would make it so there is no particles when a player renames a pet

                    Default: {default}""");
    ConfigEntry<Boolean> PARTICLES_TELEPORT_TOGGLE = REGISTRY.register("Particles.Teleport", true,
            """
                    Disabling this would make it so there is no particles when a players pet teleports to its owner

                    Default: {default}""");
    ConfigEntry<Boolean> PARTICLES_FAILED_TASK_TOGGLE = REGISTRY.register("Particles.Failed-Task", true,
            """
                    Disabling this would make it so there is no particles when a task for a pet fails

                    Default: {default}""");


    // Pathfinding
    ConfigEntry<Integer> PATHFINDING_MIN_DISTANCE = REGISTRY.register("pathfinding.min-distance-to-player", 5,
            """
                    This is the minimum distance a pet can be to a player
                    Note: This is in reference to a random location around the player the pet could be closer then this

                    Default: {default}""");
    ConfigEntry<Integer> PATHFINDING_MAX_DISTANCE = REGISTRY.register("pathfinding.max-distance-to-player", 10,
            """
                    This is the maximum stopping distance from the player

                    Default: {default}""");
    ConfigEntry<Boolean> PATHFINDING_FOLLOW_WHEN_RIDING = REGISTRY.register("pathfinding.follow-when-inside-vehicle", true,
            """
                    Should the pets follow the player when they are in a vehicle (or riding another pet)?

                    Default: {default}""");
    ConfigEntry<Integer> PATHFINDING_TELEPORT_DISTANCE = REGISTRY.register("pathfinding.distance-till-teleport", 1000,
            """
                    How far away from the player does the pet have to be before it teleports closer

                    Default: {default}""");


    ConfigEntry<Boolean> LEGACY_PATHFINDING_ENABLED = REGISTRY.register("legacy-pathfinding.enabled", false,
            """
                    Would you rather use the legacy pathfinder?

                    Note:
                    - When enabled you will not receive support for issues pertaining to this pathfinder as our main priority is the new system
                    - There are some issues with the legacy pathfinder that is known to cause high TPS lag on servers (this was fixed with the new system)
                      See: https://github.com/brainsynder-Dev/SimplePets/issues/260

                    Default: {default}""");
    ConfigEntry<Integer> LEGACY_PATHFINDING_STOP_DISTANCE_LARGE = REGISTRY.register("legacy-pathfinding.distance-to-player.large.walk", 7,
            """
                    How far away can the pet be before it will stop walking near the player?
                    Large pets can be considered: Ravager, Ghast, Giant, Wither, ETC...

                    Explanation: Pet is walking to player, they will stop within {default} blocks of the player

                    Default: {default}""");
    ConfigEntry<Integer> LEGACY_PATHFINDING_STOP_DISTANCE_SMALL = REGISTRY.register("legacy-pathfinding.distance-to-player.small.walk", 7,
            """
                    How far away can the pet be before it will stop walking near the player?
                    Small pets can be considered: cow, chicken, enderman, armorstand, ETC...

                    Explanation: Pet is walking to player, they will stop within {default} blocks of the player

                    Default: {default}""");
    ConfigEntry<Integer> LEGACY_PATHFINDING_UPDATE_INTERVAL = REGISTRY.register("legacy-pathfinding.update-interval", 10,
            """
                    Modify this value to control how often a pet tries to calculate a new path to its owner
                    Note: This is in ticks ( 20 ticks = 1 second )

                    Default: {default}""");


    // Worlds
    ConfigEntry<Boolean> WORLDS_ENABLED = REGISTRY.register("Worlds.Enabled", false,
            """
                    Enabling this will make it so pets only work in the worlds that are listed in 'Allowed-Worlds'

                    Default: {default}""");
    ConfigEntry<List<String>> WORLDS_ALLOWED_WORLDS = REGISTRY.register("Worlds.Allowed-Worlds", Lists.newArrayList("world"),
            """
                    List the worlds you want pets to be allowed to be spawned in

                    Default: {default}""");
    ConfigEntry<String> WORLDS_FAIL_MESSAGE = REGISTRY.register("Worlds.fail-message", "&cPets are not allowed to spawn in this world",
            """
                    This message is only visible when the player hovers over the fail message

                    Default: {default}""");


    // MySQL
    ConfigEntry<Boolean> MYSQL_ENABLED = REGISTRY.register("MySQL.Enabled", false,
            """
                    Would you like to use MySQL to save player/pet data?
                    If this is disabled the plugin will save all the data using SQLite

                    Default: {default}""");
    ConfigEntry<String> MYSQL_TABLE = REGISTRY.register("MySQL.Table", "simplepets",
            """
                    Default: {default}""");
    ConfigEntry<String> MYSQL_HOST = REGISTRY.register("MySQL.Host", "localhost",
            """
                    Default: {default}""");
    ConfigEntry<Integer> MYSQL_PORT = REGISTRY.register("MySQL.Port", 3306,
            """
                    Default: {default}""");
    ConfigEntry<String> MYSQL_DATABASE = REGISTRY.register("MySQL.DatabaseName", "SimplePets",
            """
                    Default: {default}""");
    ConfigEntry<String> MYSQL_USERNAME = REGISTRY.register("MySQL.Login.Username", "username");
    ConfigEntry<String> MYSQL_PASSWORD = REGISTRY.register("MySQL.Login.Password", "password");
    ConfigEntry<Boolean> MYSQL_SSL = REGISTRY.register("MySQL.Options.UseSSL", false,
            """
                    Default: {default}""");


    // Debug
    ConfigEntry<Boolean> DEBUG_ENABLED = REGISTRY.register("Debug.Enabled", true,
            """
                    Would you like to view Debug information in the console/logs?
                    It can help you locate any issues or warnings

                    Default: {default}""");
    ConfigEntry<Boolean> DEBUG_NORMAL_LEVEL = REGISTRY.register("Debug.Levels.NORMAL", true,
            """
                    This level of debug information is usually of little importance and just shows some hints

                    Default: {default}""");
    ConfigEntry<Boolean> DEBUG_WARNING_LEVEL = REGISTRY.register("Debug.Levels.WARNING", true,
            """
                    This level of debug information is usually for when there is a minor issue like a missing config value

                    Default: {default}""");
    ConfigEntry<Boolean> DEBUG_ERROR_LEVEL = REGISTRY.register("Debug.Levels.ERROR", true,
            """
                    This level of debug information is usually sent when there is an issue that NEEDS to be addressed

                    Default: {default}""");


    // Misc-Toggles
    ConfigEntry<Boolean> MISC_TOGGLES_IGNORE_ALLOWS_DEFAULT = REGISTRY.register("misc-toggles.ignore-allows-default", false,
            """
                    Should the help command ignore commands that are allowed by default
                    Example: Could be used to hide commands they do not have permission to

                    Default: {default}""").pastPaths("ConfigToggles.IgnoreAllowsDefault");
    ConfigEntry<Boolean> MISC_TOGGLES_LIST_RESTRICTIONS = REGISTRY.register("misc-toggles.restrict-pet-list", false,
            """
                    Setting this to true will hide any Unregistered, Unsupported, and only show the pets the player can use/purchase in `/pet list`.

                    Default: {default}""").pastPaths("ConfigToggles.HideCertainPets");
    ConfigEntry<Boolean> MISC_TOGGLES_AUTO_CLOSE_REMOVE = REGISTRY.register("misc-toggles.auto-closing.on-pet-remove", true,
            """
                    Should the Pet GUI close when they remove a pet

                    Default: {default}""").pastPaths("InventoryToggles.AutoClosing.RemovePet");
    ConfigEntry<Boolean> MISC_TOGGLES_AUTO_CLOSE_RIDE = REGISTRY.register("misc-toggles.auto-closing.on-pet-ride", true,
            """
                    Should the Pet GUI close when they start riding a pet

                    Default: {default}""").pastPaths("InventoryToggles.AutoClosing.RidePet");
    ConfigEntry<Boolean> MISC_TOGGLES_AUTO_CLOSE_HAT = REGISTRY.register("misc-toggles.auto-closing.on-pet-hat", true,
            """
                    Should the Pet GUI close when they set a pet as a hat

                    Default: {default}""").pastPaths("InventoryToggles.AutoClosing.HatPet");
    ConfigEntry<Boolean> MISC_TOGGLES_REMOVE_ALL_PETS = REGISTRY.register("misc-toggles.remove-all-pets", true,
            """
                    When clicking the "Remove Pet" item should all pets be removed
                        (If true, the only way to select a specific pet would be to Shift-Click the item)
                    Or should it open the Selection GUI to pick a pet to remove

                    Default: {default}""");
    ConfigEntry<Boolean> MISC_TOGGLES_CLEAR_ALL_PLACEHOLDERS = REGISTRY.register("misc-toggles.remove-all-placeholders", false,
            """
                    This option allows for the removal of the placeholder items (The glass panes)

                    NOTE: these are still required to be in the GUI, this will simply remove them afterwards

                    Default: {default}""");
    ConfigEntry<Boolean> MISC_TOGGLES_CONSOLE_BYPASS_LIMIT = REGISTRY.register("misc-toggles.console-bypasses-pet-limit", true,
            """
                    When summoning a pet for a player via the servers console should the pet limit be enforced

                    Default: {default}""");
    ConfigEntry<Boolean> MISC_TOGGLES_DISABLE_CLICKING = REGISTRY.register("misc-toggles.disable-pet-right-clicking", false,
            """
                    Should pet owners be able to open their pets data menu when right clicking the pet.

                    Default: {default}""");
    ConfigEntry<Boolean> MISC_TOGGLES_PET_VANISHING = REGISTRY.register("misc-toggles.vanishing.pet-visibility", true,
            """
                    Should pets turn invisible when their owner is not visible (either via Spectator or Invisibility potions)

                    Default: {default}""").pastPaths("misc-toggles.enable-pet-vanishing");
    ConfigEntry<Boolean> MISC_TOGGLES_REMOVED_VANISH = REGISTRY.register("misc-toggles.vanishing.remove-pet-when-vanished", false,
            """
                    When the owner is vanished should the pets the player has active be removed?

                    NOTE: This will override the 'pet-visibility' option

                    Default: {default}""");
    ConfigEntry<Boolean> MISC_TOGGLES_WORLD_CONFINES_PET_LIMITS = REGISTRY.register("misc-toggles.world-confines-pet-limits.enabled", false,
            """
                    When pets are outside of the vertical confines of the world they will
                    be removed and more pets will be prevented from spawning

                    Note: This is recommended if you are experiencing lag with pets and players being high up in the world
                    Note: This MIGHT be removed in the future when a permanent fix is implemented

                    Default: {default}""");
    ConfigEntry<String> MISC_TOGGLES_EXCEEDS_WORLD_CONFINES = REGISTRY.register("misc-toggles.world-confines-pet-limits.spawning-error-message",
            "&cPets are not allowed to be spawned outside of the worlds vertical confines",
            """
                    When pets are outside of the vertical confines of the world they will
                    be removed and more pets will be prevented from spawning

                    Note: This is recommended if you are experiencing lag with pets and players being high up in the world
                    Note: This MIGHT be removed in the future when a permanent fix is implemented

                    Default: {default}""");
    ConfigEntry<Boolean> MISC_TOGGLES_SPAWN_FAILURE_DIAGNOSTICS = REGISTRY.register("misc-toggles.spawn-failure-diagnostics", true,
            """
                    When a pet fails to spawn, should SimplePets try to work out which plugin blocked it?

                    Note: Disable this if another plugin misbehaves when it sees the same spawn event twice

                    Default: {default}""");


    // Pet Toggles
    ConfigEntry<Boolean> PET_TOGGLES_SPAWN_BYPASS = REGISTRY.register("pet-toggles.mob-spawn-bypass", true,
            """
                    When this is enabled it will allow pets to spawn anywhere
                    This is especially used if WorldGuard/PlotSquared are blocking mobs from spawning

                    Default: {default}""").pastPaths("PetToggles.MobSpawnBypass");
    ConfigEntry<Boolean> PET_TOGGLES_DEV_MOBS = REGISTRY.register("pet-toggles.allow-in-development-mobs", false,
            """
                    If this is true it will allow any mobs that are currently in-development to be spawned/registered

                    Default: {default}""").pastPaths("PetToggles.Allow-In-Development-Mobs");
    ConfigEntry<Boolean> PET_TOGGLES_MOB_PUSHER = REGISTRY.register("pet-toggles.move-pets-get-out-da-way", false,
            """
                    Are pets able to be pushed around

                    Default: {default}""").pastPaths("PetToggles.Move-Pets-Get-Out-Da-Way");
    ConfigEntry<Boolean> PET_TOGGLES_FALL_DAMAGE_NON_FLY = REGISTRY.register("pet-toggles.fall-damage.non-flying-pets", true,
            """
                    Should fall damage be allowed for Non-Flyable pets?
                    Example: cow, pig, goat, etc...

                    Default: {default}""").pastPaths("PetToggles.FallDamage.Non-Flyable-Pets");
    ConfigEntry<Boolean> PET_TOGGLES_FALL_DAMAGE_FLY = REGISTRY.register("pet-toggles.fall-damage.flying-pets", true,
            """
                    Should fall damage be allowed for Flyable pets?
                    Example: bat, bee, parrot, etc...

                    Default: {default}""").pastPaths("PetToggles.Flyable-Pets");
    ConfigEntry<Boolean> PET_TOGGLES_SPIDER_CLIMB = REGISTRY.register("pet-toggles.traits.spider-wall-climbing", true,
            """
                    Are spider type pets able to climb walls?

                    Default: {default}""").pastPaths("PetToggles.SpiderClimbing");
    ConfigEntry<Boolean> PET_TOGGLES_WARDEN_ANIMATIONS = REGISTRY.register("pet-toggles.traits.warden-animations", true,
            """
                    Should the spawning/de-spawning animation for the warden be used or just make them appear/disappear

                    Default: {default}""");
    ConfigEntry<Boolean> PET_TOGGLES_GLOW_VANISH = REGISTRY.register("pet-toggles.traits.glow-when-vanished", true,
            """
                    When the owner is vanished should the owner see their pet with the glow effect?
                    The owner of the pet is the only one able to see it

                    Default: {default}""").pastPaths("PetToggles.GlowWhenVanished");
    ConfigEntry<Boolean> PET_TOGGLES_SHOW_NAMES = REGISTRY.register("pet-toggles.show-pet-names", true,
            """
                    Should pet names be seen?

                    Default: {default}""").pastPaths("PetToggles.ShowPetNames");
    ConfigEntry<Boolean> PET_TOGGLES_SHIFT_HIDDEN_NAMES = REGISTRY.register("pet-toggles.hide-pet-names-when-shifting", true,
            """
                    Should pet names be hidden when their owner sneaks?

                    Default: {default}""").pastPaths("PetToggles.HideNameOnShift");
    ConfigEntry<Boolean> PET_TOGGLES_MOUNTABLE = REGISTRY.register("pet-toggles.all-pets-are-mountable", true,
            """
                    Are all pets able to be rideable?
                    When this is set to false only pets that have 'mount' set to true in their JSON file will be allowed

                    Default: {default}""").pastPaths("PetToggles.All-Pets-Mountable");
    ConfigEntry<Boolean> PET_TOGGLES_HAT = REGISTRY.register("pet-toggles.all-pets-hat", true,
            """
                    Are all pets able to be worn as hats?
                    When this is set to false only pets that have 'hat' set to true in their JSON file will be allowed

                    Default: {default}""").pastPaths("PetToggles.All-Pets-Hat");
    ConfigEntry<Boolean> PET_TOGGLES_FLYABLE = REGISTRY.register("pet-toggles.all-pets-flyable", false,
            """
                    Are all pets able to fly when mounted?
                    When this is set to false only pets that have 'fly' set to true in their JSON file will be allowed

                    Default: {default}""").pastPaths("PetToggles.All-Pets-Flyable");
    ConfigEntry<Integer> PET_TOGGLES_SPAWN_LIMIT = REGISTRY.register("pet-toggles.pet-spawn-limit", 3,
            """
                    The maximum number of pets a player can spawn at a time.
                    This can be overridden using pet.amount.<Number>, e.g. pet.amount.1 to only allow 1 at once.

                    Default: {default}""").pastPaths("PetToggles.Default-Spawn-Limit");
    ConfigEntry<Double> PET_TOGGLES_WALK_SPEED = REGISTRY.register("pet-toggles.default-pet-walk-speed", 0.580804838418579,
            """
                    The default walk speed each pet will go at.
                    This can be overridden in an individual pet JSON file using the 'walk_speed' key.

                    Default: {default}""").pastPaths("PetToggles.Default-Walk-Speed");
    ConfigEntry<Double> PET_TOGGLES_RIDE_SPEED = REGISTRY.register("pet-toggles.default-pet-ride-speed", 0.200004838418579,
            """
                    The default ride speed each pet will go at when mounted.
                    This can be overridden in an individual pet .json file using the ride_speed key.

                    Default: {default}""").pastPaths("PetToggles.Default-Ride-Speed");
    ConfigEntry<Double> PET_TOGGLES_FLY_SPEED = REGISTRY.register("pet-toggles.default-pet-fly-speed", 0.300000438418579,
            """
                    The default ride speed each pet will go at when mounted.
                    This can be overridden in an individual pet .json file using the ride_speed key.

                    Default: {default}""").pastPaths("PetToggles.Default-Ride-Speed");
    ConfigEntry<Double> PET_TOGGLES_WATER_SPEED = REGISTRY.register("pet-toggles.default-pet-water-speed", 0.15,
            """
                    The default speed a pet moves at when being ridden on or through water.
                    This can be overridden in an individual pet .json file using the water_speed key.

                    Default: {default}""");


    ConfigEntry<Boolean> PET_SAVES_ENABLED = REGISTRY.register("pet-saves.enabled", true,
            """
                    Do you want players to be able to save the pets they have spawned
                    so they don't have to re-customize the pet to their state

                    This option will essentially disable the Saves GUI/Item

                    Default: {default}""");
    ConfigEntry<Integer> PET_SAVES_LIMIT = REGISTRY.register("pet-saves.default-limit", -1,
            """
                    How many pets do you want your players to be able to save?
                    Set this to `-1` if you want no limit

                    Can be overrode via the permission `pet.saves.<amount>`
                    Bypass permission: `pet.saves.bypass`

                    Default: {default}""");
    ConfigEntry<List<String>> PET_SAVES_TYPE_LIMIT = REGISTRY.register("pet-saves.pet-type-limits", Lists.newArrayList(""),
            """
                    This option allows you to list pet types and the limit for how many saves they can have

                    Example: "COW-2"
                    This example will make it so the COW pet type can only be saved 2 times

                    Can be overrode via the permission `pet.saves.<type>.<amount>`
                    Bypass permission: `pet.saves.bypass`
                    Bypass permission: `pet.saves.<type>.bypass`

                    Default: {default}""");


    ConfigEntry<Boolean> RENAME_ENABLED = REGISTRY.register("RenamePet.Enabled", true,
            """
                    Should players be able to rename pets?
                    This will also control if the '/pet rename' and also the 'Rename Pet' item are available

                    Default: {default}""");
    ConfigEntry<String> RENAME_TYPE = REGISTRY.register("RenamePet.Type", "ANVIL",
            """
                    How should the player be able to modify their pets name?
                    Types:
                    - COMMAND (They have to use the '/pet rename' command to set the name)
                    - CHAT (They have to type the name in chat)
                    - ANVIL (The Anvil GUI will open and they can change the name there)
                    - SIGN [REQUIRES ProtocolLib] (Will open a Sign GUI they input the name on the configured line)
                    - DIALOG (Will open a dialog for them to enter their pets new name)

                    Default: {default}""");
    ConfigEntry<Boolean> RENAME_TRIM = REGISTRY.register("RenamePet.Trim-Name", false,
            """
                    Should there be spaces at the start and end of the name?

                    Default: {default}""");
    ConfigEntry<Boolean> RENAME_LIMIT_CHARS_ENABLED = REGISTRY.register("RenamePet.Character-Limit.Enabled", false,
            """
                    Should the name have a limited number of characters?

                    Default: {default}""");
    ConfigEntry<Integer> RENAME_LIMIT_CHARS_NUMBER = REGISTRY.register("RenamePet.Character-Limit.Limit", 10,
            """
                    What should the character limit be set to?

                    Default: {default}""");
    ConfigEntry<List<String>> RENAME_BLOCKED_WORDS = REGISTRY.register("RenamePet.Blocked-Words", Lists.newArrayList("jeb_"),
            """
                    Are there words you don't want in a pets name?
                       If You put an ^ at the start of the word/text it will ignore case
                             Example: ^ass will also flag ASS because it will ignore what case the letters are
                       If you put word in between () it will check if it contains ANY part of the word
                             Example: (ass) will also flag glass because it contains the word in it
                       If you just have the word it will check for the exact word
                             Example: 'ass' will just be flagged so 'glass' can be said

                    Default: {default}""");
    ConfigEntry<String> RENAME_BLOCKED_PATTERN = REGISTRY.register("RenamePet.Blocked-RegexPattern", "",
            """
                    This blocks pets from having anything that matches the pattern as the name
                    For example MergedMobs has a pattern of '([0-9]+)(x)' will block names that are '999x'
                    If you want to use that pattern but ignore case then use '/([0-9]+)(x)/gmi'
                    ONLY CHECKS IF THE NAME MATCHES THE PATTERN

                    Default: {default}""");
    ConfigEntry<Boolean> RENAME_COLOR_MAGIC = REGISTRY.register("RenamePet.Allow-&k", false,
            """
                    Are pet names allowed to have the &k color code?

                    Default: {default}""");
    ConfigEntry<Boolean> RENAME_COLOR_ENABLED = REGISTRY.register("RenamePet.ColorCodes.Enabled", true,
            """
                    Are pet names allowed to be colored?

                    Default: {default}""");
    ConfigEntry<Boolean> RENAME_COLOR_HEX = REGISTRY.register("RenamePet.ColorCodes.Allow-HEX-Colors", true,
            """
                    Are pet names allowed to have HEX colors?
                    NOTE: If this is disabled only regular chat color will be used (EG: '&c')
                    Example: '&#c986b2'

                    Default: {default}""");

    ConfigEntry<Boolean> PET_COOLDOWN_ENABLED = REGISTRY.register("pet-cooldown.enabled", false,
            """
                    When enabled, players must wait a set duration before spawning another pet.
                    Bypass permission: pet.cooldown.bypass

                    Default: {default}""");
    ConfigEntry<Integer> PET_COOLDOWN_SECONDS = REGISTRY.register("pet-cooldown.duration", 5,
            """
                    How many seconds a player must wait between spawning pets.
                    Only applies when 'pet-cooldown.enabled' is true.

                    Default: {default}""");
}
