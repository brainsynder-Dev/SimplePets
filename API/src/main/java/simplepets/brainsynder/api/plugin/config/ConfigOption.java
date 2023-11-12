package simplepets.brainsynder.api.plugin.config;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public enum ConfigOption {
    INSTANCE;
    private final Map<String, ConfigEntry> options = new LinkedHashMap<>();

    public final ConfigEntry<Boolean> RELOAD_DETECT = createOption("Reload-Detected", false,
            """
            This is used by the plugin to detect if the plugin was previously unloaded (via /reload or by a plugin)
                CAN NOT BE CUSTOMIZED
            """);
    public final ConfigEntry<Boolean> SIMPLER_GUI = createOption("Simpler-Pet-GUI-Command", false,
            """
                    UGGGGGGGG This config option makes it so `/pet` opens the GUI (like `/pet gui`)
                    Requires a server restart for some reason ¯\\_(ツ)_/¯
                    
                    Default: {default}""");

    // Update Checking
    public final ConfigEntry<Boolean> UPDATE_CHECK_ENABLED = createOption("Update-Checking.Enabled", true,
            """
                    Would you like to check for when there is a new update?
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> UPDATE_CHECK_ON_JOIN = createOption("Update-Checking.Message-On-Join", true,
            """
                    Would you like to be alerted when there is a new update when you log in?
                    (MUST HAVE 'pet.update' permission or OP)
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> UPDATE_CHECK_DEV_BUILDS = createOption("Update-Checking.Check-Dev-Builds", true,
            """
                    This will enable checking for dev builds released on jenkins
                    (This will only be used when you have downloaded the jar from either Polymart or Spigot)
                    
                    Default: {default}""");
    public final ConfigEntry<String> UPDATE_CHECK_UNIT = createOption("Update-Checking.unit", TimeUnit.HOURS.name(),
            """
                    The unit of time for update checking
                    Time Units:
                    - SECONDS
                    - MINUTES
                    - HOURS
                    - DAYS
                    
                    Default: {default}""");
    public final ConfigEntry<Integer> UPDATE_CHECK_TIME = createOption("Update-Checking.time", 12);

    // Permissions
    public final ConfigEntry<List<String>> PERMISSIONS_IGNORE_LIST = createOption("Permissions.Ignored-List", new ArrayList<>(),
            """
                    Any permission in this list will be ignored when the plugin checks if the player has permission
                    (Will act like the player has the permission, without actually having it)
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> PERMISSIONS_ENABLED = createOption("Permissions.Enabled", true,
            """
                    Disabling this would grant ALL players access to pets (they wont need permissions)
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> PERMISSIONS_OPEN_GUI = createOption("Permissions.Needs-Pet-Permission-for-GUI", false,
            """
                    Enabling this would require players to have access to at least 1 pets permission
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> PERMISSIONS_DATA_PERMS = createOption("Permissions.Data-Permissions", true,
            """
                    Disabling this will make it so players do not need to have any data permissions (EG. pet.type.armorstand.data.silent)
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> PERMISSIONS_PLAYER_ACCESS = createOption("Permissions.Only-Show-Pets-Player-Can-Access", true,
            """
                    Enabling this would remove all the pets the player does not have access to from the GUI
                    
                    Default: {default}""");


    // Addon Config
    public final ConfigEntry<String> ADDON_LOAD_UNIT = createOption("addon-initialization.unit", TimeUnit.SECONDS.name(),
            """
                    The unit of time for when the addons should be loaded
                    
                    Time Units:
                    - SECONDS
                    - MINUTES
                    - HOURS
                    - DAYS
                    
                    Default: {default}""");
    public final ConfigEntry<Integer> ADDON_LOAD_TIME = createOption("addon-initialization.time", 5, """
                    The integer value which correlates to the selected unit
                    
                    Example:
                        unit: HOURS
                        time: 12
                    Will be 12 Hours till the addons initiate
                    
                    Default: {default}
                    """);



    public final ConfigEntry<Boolean> REMOVE_PET_ON_WORLD_CHANGE = createOption("RemovePetsOnWorldChange", true,
            """
                    Disabling this will remove a players pet when they change worlds
                    
                    Default: {default}""");


    public final ConfigEntry<Boolean> RESPAWN_LAST_PET_LOGIN = createOption("Respawn-Last-Pet-On-Login", true,
            """
                    When a player logs back in should their pet be spawned in as well?
                    NOTE: If the player removed their pet before logging out then it wont respawn.
                    
                    Default: {default}""");


    public final ConfigEntry<Boolean> UTILIZE_PURCHASED_PETS = createOption("Utilize-Purchased-Pets", false,
            """
                    This option will make it so if the player has the pet (was 'purchased' either via economy or the command)
                    It will allow the player to spawn that pet regardless if they have permission to it
                    
                    Default: {default}""");


    public final ConfigEntry<Boolean> AUTO_REMOVE_ENABLED = createOption("auto-remove.enabled", true,
            """
                    Disabling this will make it so pets wont be automatically removed if the player is afk
                    
                    Default: {default}""").setPastPaths("PetToggles.AutoRemove.Enabled");
    public final ConfigEntry<Integer> AUTO_REMOVE_TICK = createOption("auto-remove.tick-delay", 10000,
            """
                    What should the wait be?
                    This is in ticks (20 ticks = 1 second)
                    Example: 10000 ≈ 8 minutes 20 seconds
                    
                    Default: {default}""").setPastPaths("PetToggles.AutoRemove.TickDelay");


    // Particle Toggles
    public final ConfigEntry<Boolean> PARTICLES_SUMMON_TOGGLE = createOption("Particles.Summon", true,
            """
                    Disabling this would make it so there is no particles when a player spawns a pet
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> PARTICLES_FAILED_TOGGLE = createOption("Particles.Failed", true,
            """
                    Disabling this would make it so there is no particles when a task fails for a player (EG: spawning, riding, etc..)
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> PARTICLES_REMOVE_TOGGLE = createOption("Particles.Remove", true,
            """
                    Disabling this would make it so there is no particles when a player removes a pet
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> PARTICLES_RENAME_TOGGLE = createOption("Particles.Name-Change", true,
            """
                    Disabling this would make it so there is no particles when a player renames a pet
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> PARTICLES_TELEPORT_TOGGLE = createOption("Particles.Teleport", true,
            """
                    Disabling this would make it so there is no particles when a players pet teleports to its owner
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> PARTICLES_FAILED_TASK_TOGGLE = createOption("Particles.Failed-Task", true,
            """
                    Disabling this would make it so there is no particles when a task for a pet fails
                    
                    Default: {default}""");


    // Pathfinding
    public final ConfigEntry<Double> PATHFINDING_DISTANCE_SMALL = createOption("pathfinding.distance-to-player.small.stand", 1.9,
            """
                    How far away can the pets stand near the player?
                    Small pets can be considered: cow, chicken, enderman, armorstand, ETC...
                    
                    Explanation: Player is walking around the pet wont start to follow unless they are {default} blocks away
                    Default: {default}""").setPastPaths("Pathfinding.Distance-to-Player");
    public final ConfigEntry<Double> PATHFINDING_DISTANCE_LARGE = createOption("pathfinding.distance-to-player.large.stand", 2.9,
            """
                    How far away can the large pets stand near the player?
                    Large pets can be considered: Ravager, Ghast, Giant, Wither, ETC...
                    
                    Explanation: Player is walking around the pet wont start to follow unless they are {default} blocks away
                    Default: {default}""").setPastPaths("Pathfinding.Distance-to-Player_LargePets");
    public final ConfigEntry<Integer> PATHFINDING_STOP_DISTANCE_SMALL = createOption("pathfinding.distance-to-player.small.walk", 3,
            """
                    How far away can the pet be before it will stop walking near the player?
                    Small pets can be considered: cow, chicken, enderman, armorstand, ETC...
                    
                    Explanation: Pet is walking to player, they will stop within {default} blocks of the player
                    Default: {default}""").setPastPaths("Pathfinding.Stopping-Distance");
    public final ConfigEntry<Integer> PATHFINDING_STOP_DISTANCE_LARGE = createOption("pathfinding.distance-to-player.large.walk", 7,
            """
                    How far away can the pet be before it will stop walking near the player?
                    Large pets can be considered: Ravager, Ghast, Giant, Wither, ETC...
                    
                    Explanation: Pet is walking to player, they will stop within {default} blocks of the player
                    Default: {default}""").setPastPaths("Pathfinding.Stopping-Distance_LargePets");
    public final ConfigEntry<Boolean> PATHFINDING_FOLLOW_WHEN_RIDING = createOption("pathfinding.follow-when-inside-vehicle", true,
            """
                    Should the pets follow the player when they are in a vehicle (or riding another pet)?
                    
                    Default: {default}""");
    public final ConfigEntry<Integer> PATHFINDING_MINIMUM_TELEPORT = createOption("pathfinding.distance-till-teleport", 20,
            """
                    How far away from the player does the pet have to be before it teleports closer
                    This is the minimum distance required, there could be other reasons for a teleport.
                    
                    Default: {default}""").setPastPaths("Pathfinding.Min-Distance-For-Teleport");


    // Worlds
    public final ConfigEntry<Boolean> WORLDS_ENABLED = createOption("Worlds.Enabled", false,
            """
                    Enabling this will make it so pets only work in the worlds that are listed in 'Allowed-Worlds'
                    
                    Default: {default}""");
    public final ConfigEntry<List<String>> WORLDS_ALLOWED_WORLDS = createOption("Worlds.Allowed-Worlds", Lists.newArrayList("world"),
            """
                    List the worlds you want pets to be allowed to be spawned in
                    
                    Default: {default}""");
    public final ConfigEntry<String> WORLDS_FAIL_MESSAGE = createOption("Worlds.fail-message", "&cPets are not allowed to spawn in this world",
            """
                    This message is only visible when the player hovers over the fail message
                    
                    Default: {default}""");


    // MySQL
    public final ConfigEntry<Boolean> MYSQL_ENABLED = createOption("MySQL.Enabled", false,
            """
                    Would you like to use MySQL to save player/pet data?
                    If this is disabled the plugin will save all the data using SQLite
                    
                    Default: {default}""");
    public final ConfigEntry<String> MYSQL_TABLE = createOption("MySQL.Table", "simplepets",
            """
                    Default: {default}""");
    public final ConfigEntry<String> MYSQL_HOST = createOption("MySQL.Host", "localhost",
            """
                    Default: {default}""");
    public final ConfigEntry<Integer> MYSQL_PORT = createOption("MySQL.Port", 3306,
            """
                    Default: {default}""");
    public final ConfigEntry<String> MYSQL_DATABASE = createOption("MySQL.DatabaseName", "SimplePets",
            """
                    Default: {default}""");
    public final ConfigEntry<String> MYSQL_USERNAME = createOption("MySQL.Login.Username", "username");
    public final ConfigEntry<String> MYSQL_PASSWORD = createOption("MySQL.Login.Password", "password");
    public final ConfigEntry<Boolean> MYSQL_SSL = createOption("MySQL.Options.UseSSL", false,
            """
                    Default: {default}""");


    // Debug
    public final ConfigEntry<Boolean> DEBUG_ENABLED = createOption("Debug.Enabled", true,
            """
                    Would you like to view Debug information in the console/logs?
                    It can help you locate any issues or warnings
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> DEBUG_NORMAL_LEVEL = createOption("Debug.Levels.NORMAL", true,
            """
                    This level of debug information is usually of little importance and just shows some hints
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> DEBUG_WARNING_LEVEL = createOption("Debug.Levels.WARNING", true,
            """
                    This level of debug information is usually for when there is a minor issue like a missing config value
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> DEBUG_ERROR_LEVEL = createOption("Debug.Levels.ERROR", true,
            """
                    This level of debug information is usually sent when there is an issue that NEEDS to be addressed
                    
                    Default: {default}""");


    // Misc-Toggles
    public final ConfigEntry<Boolean> MISC_TOGGLES_IGNORE_ALLOWS_DEFAULT = createOption("misc-toggles.ignore-allows-default", false,
            """
                    Should the help command ignore commands that are allowed by default
                    Example: Could be used to hide commands they do not have permission to
                    
                    Default: {default}""").setPastPaths("ConfigToggles.IgnoreAllowsDefault");
    public final ConfigEntry<Boolean> MISC_TOGGLES_LIST_RESTRICTIONS = createOption("misc-toggles.restrict-pet-list", false,
            """
                    Setting this to true will hide any Unregistered, Unsupported, and only show the pets the player can use/purchase in `/pet list`.
                    
                    Default: {default}""").setPastPaths("ConfigToggles.HideCertainPets");
    public final ConfigEntry<Boolean> MISC_TOGGLES_AUTO_CLOSE_REMOVE = createOption("misc-toggles.auto-closing.on-pet-remove", true,
            """
                    Should the Pet GUI close when they remove a pet
                    
                    Default: {default}""").setPastPaths("InventoryToggles.AutoClosing.RemovePet");
    public final ConfigEntry<Boolean> MISC_TOGGLES_AUTO_CLOSE_RIDE = createOption("misc-toggles.auto-closing.on-pet-ride", true,
            """
                    Should the Pet GUI close when they start riding a pet
                    
                    Default: {default}""").setPastPaths("InventoryToggles.AutoClosing.RidePet");
    public final ConfigEntry<Boolean> MISC_TOGGLES_AUTO_CLOSE_HAT = createOption("misc-toggles.auto-closing.on-pet-hat", true,
            """
                    Should the Pet GUI close when they set a pet as a hat
                    
                    Default: {default}""").setPastPaths("InventoryToggles.AutoClosing.HatPet");
    public final ConfigEntry<Boolean> MISC_TOGGLES_REMOVE_ALL_PETS = createOption("misc-toggles.remove-all-pets", true,
            """
                    When clicking the "Remove Pet" item should all pets be removed
                        (If true, the only way to select a specific pet would be to Shift-Click the item)
                    Or should it open the Selection GUI to pick a pet to remove
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> MISC_TOGGLES_CLEAR_ALL_PLACEHOLDERS = createOption("misc-toggles.remove-all-placeholders", false,
            """
                    This option allows for the removal of the placeholder items (The glass panes)
                    
                    NOTE: these are still required to be in the GUI, this will simply remove them afterwards
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> MISC_TOGGLES_CONSOLE_BYPASS_LIMIT = createOption("misc-toggles.console-bypasses-pet-limit", true,
            """
                    When summoning a pet for a player via the servers console should the pet limit be enforced
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> MISC_TOGGLES_DISABLE_CLICKING = createOption("misc-toggles.disable-pet-right-clicking", false,
            """
                    Should pet owners be able to open their pets data menu when right clicking the pet.
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> MISC_TOGGLES_PET_VANISHING = createOption("misc-toggles.vanishing.pet-visibility", true,
            """
                    Should pets turn invisible when their owner is not visible (either via Spectator or Invisibility potions)

                    Default: {default}""").setPastPaths("misc-toggles.enable-pet-vanishing");
    public final ConfigEntry<Boolean> MISC_TOGGLES_REMOVED_VANISH = createOption("misc-toggles.vanishing.remove-pet-when-vanished", false,
            """
                    When the owner is vanished should the pets the player has active be removed?
                    
                    NOTE: This will override the 'pet-visibility' option
                    
                    Default: {default}""");


    // Pet Toggles
    public final ConfigEntry<Boolean> PET_TOGGLES_SPAWN_BYPASS = createOption("pet-toggles.mob-spawn-bypass", true,
            """
                    When this is enabled it will allow pets to spawn anywhere
                    This is especially used if WorldGuard/PlotSquared are blocking mobs from spawning
                    
                    Default: {default}""").setPastPaths("PetToggles.MobSpawnBypass");
    public final ConfigEntry<Boolean> PET_TOGGLES_DEV_MOBS = createOption("pet-toggles.allow-in-development-mobs", false,
            """
                    If this is true it will allow any mobs that are currently in-development to be spawned/registered
                    
                    Default: {default}""").setPastPaths("PetToggles.Allow-In-Development-Mobs");
    public final ConfigEntry<Boolean> PET_TOGGLES_MOB_PUSHER = createOption("pet-toggles.move-pets-get-out-da-way", false,
            """
                    Are pets able to be pushed around
                    
                    Default: {default}""").setPastPaths("PetToggles.Move-Pets-Get-Out-Da-Way");
    public final ConfigEntry<Boolean> PET_TOGGLES_FALL_DAMAGE_NON_FLY = createOption("pet-toggles.fall-damage.non-flying-pets", true,
            """
                    Should fall damage be allowed for Non-Flyable pets?
                    Example: cow, pig, goat, etc...
                    
                    Default: {default}""").setPastPaths("PetToggles.FallDamage.Non-Flyable-Pets");
    public final ConfigEntry<Boolean> PET_TOGGLES_FALL_DAMAGE_FLY = createOption("pet-toggles.fall-damage.flying-pets", true,
            """
                    Should fall damage be allowed for Flyable pets?
                    Example: bat, bee, parrot, etc...
                    
                    Default: {default}""").setPastPaths("PetToggles.Flyable-Pets");
    public final ConfigEntry<Boolean> PET_TOGGLES_SPIDER_CLIMB = createOption("pet-toggles.traits.spider-wall-climbing", true,
            """
                    Are spider type pets able to climb walls?
                    
                    Default: {default}""").setPastPaths("PetToggles.SpiderClimbing");
    public final ConfigEntry<Boolean> PET_TOGGLES_WARDEN_ANIMATIONS = createOption("pet-toggles.traits.warden-animations", true,
            """
                    Should the spawning/de-spawning animation for the warden be used or just make them appear/disappear
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> PET_TOGGLES_GLOW_VANISH = createOption("pet-toggles.traits.glow-when-vanished", true,
            """
                    When the owner is vanished should the owner see their pet with the glow effect?
                    The owner of the pet is the only one able to see it
                    
                    Default: {default}""").setPastPaths("PetToggles.GlowWhenVanished");
    public final ConfigEntry<Boolean> PET_TOGGLES_SHOW_NAMES = createOption("pet-toggles.show-pet-names", true,
            """
                    Should pet names be seen?
                    
                    Default: {default}""").setPastPaths("PetToggles.ShowPetNames");
    public final ConfigEntry<Boolean> PET_TOGGLES_SHIFT_HIDDEN_NAMES = createOption("pet-toggles.hide-pet-names-when-shifting", true,
            """
                    Should pet names be hidden when their owner sneaks?
                    
                    Default: {default}""").setPastPaths("PetToggles.HideNameOnShift");
    public final ConfigEntry<Boolean> PET_TOGGLES_MOUNTABLE = createOption("pet-toggles.all-pets-are-mountable", true,
            """
                    Are all pets able to be rideable?
                    When this is set to false only pets that have 'mount' set to true in their JSON file will be allowed
                    
                    Default: {default}""").setPastPaths("PetToggles.All-Pets-Mountable");
    public final ConfigEntry<Boolean> PET_TOGGLES_HAT = createOption("pet-toggles.all-pets-hat", true,
            """
                    Are all pets able to be worn as hats?
                    When this is set to false only pets that have 'hat' set to true in their JSON file will be allowed
                    
                    Default: {default}""").setPastPaths("PetToggles.All-Pets-Hat");
    public final ConfigEntry<Boolean> PET_TOGGLES_FLYABLE = createOption("pet-toggles.all-pets-flyable", false,
            """
                    Are all pets able to fly when mounted?
                    When this is set to false only pets that have 'fly' set to true in their JSON file will be allowed
                    
                    Default: {default}""").setPastPaths("PetToggles.All-Pets-Flyable");
    public final ConfigEntry<Integer> PET_TOGGLES_SPAWN_LIMIT = createOption("pet-toggles.pet-spawn-limit", 3,
            """
                    The maximum number of pets a player can spawn at a time.
                    This can be overridden using pet.amount.<Number>, e.g. pet.amount.1 to only allow 1 at once.
                    
                    Default: {default}""").setPastPaths("PetToggles.Default-Spawn-Limit");
    public final ConfigEntry<Double> PET_TOGGLES_WALK_SPEED = createOption("pet-toggles.default-pet-walk-speed", 0.580804838418579,
            """
                    The default walk speed each pet will go at.
                    This can be overridden in an individual pet JSON file using the 'walk_speed' key.
                    
                    Default: {default}""").setPastPaths("PetToggles.Default-Walk-Speed");
    public final ConfigEntry<Double> PET_TOGGLES_RIDE_SPEED = createOption("pet-toggles.default-pet-ride-speed", 0.200004838418579,
            """
                    The default ride speed each pet will go at when mounted.
                    This can be overridden in an individual pet .json file using the ride_speed key.
                    
                    Default: {default}""").setPastPaths("PetToggles.Default-Ride-Speed");
    public final ConfigEntry<Double> PET_TOGGLES_FLY_SPEED = createOption("pet-toggles.default-pet-fly-speed", 0.300000438418579,
            """
                    The default ride speed each pet will go at when mounted.
                    This can be overridden in an individual pet .json file using the ride_speed key.
                    
                    Default: {default}""").setPastPaths("PetToggles.Default-Ride-Speed");


    public final ConfigEntry<Boolean> PET_SAVES_ENABLED = createOption("pet-saves.enabled", true,
            """
                    Do you want players to be able to save the pets they have spawned
                    so they don't have to re-customize the pet to their state
                    
                    This option will essentially disable the Saves GUI/Item
                    
                    Default: {default}""");
    public final ConfigEntry<Integer> PET_SAVES_LIMIT = createOption("pet-saves.default-limit", -1,
            """
                    How many pets do you want your players to be able to save?
                    Set this to `-1` if you want no limit
                    
                    Can be overrode via the permission `pet.saves.<amount>`
                    Bypass permission: `pet.saves.bypass`
                    
                    Default: {default}""");
    public final ConfigEntry<List<String>> PET_SAVES_TYPE_LIMIT = createOption("pet-saves.pet-type-limits", Lists.newArrayList(""),
            """
                    This option allows you to list pet types and the limit for how many saves they can have
                    
                    Example: "COW-2"
                    This example will make it so the COW pet type can only be saved 2 times
                    
                    Can be overrode via the permission `pet.saves.<type>.<amount>`
                    Bypass permission: `pet.saves.bypass`
                    Bypass permission: `pet.saves.<type>.bypass`
                    
                    Default: {default}""");



    public final ConfigEntry<Boolean> RENAME_ENABLED = createOption("RenamePet.Enabled", true,
            """
                    Should players be able to rename pets?
                    This will also control if the '/pet rename' and also the 'Rename Pet' item are available
                    
                    Default: {default}""");
    public final ConfigEntry<String> RENAME_TYPE = createOption("RenamePet.Type", "ANVIL",
            """
                    How should the player be able to modify their pets name?
                    Types:
                    - COMMAND (They have to use the '/pet rename' command to set the name)
                    - CHAT (They have to type the name in chat)
                    - ANVIL (The Anvil GUI will open and they can change the name there)
                    - SIGN [REQUIRES ProtocolLib] (Will open a Sign GUI they input the name on the configured line)
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> RENAME_TRIM = createOption("RenamePet.Trim-Name", false,
            """
                    Should there be spaces at the start and end of the name?
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> RENAME_LIMIT_CHARS_ENABLED = createOption("RenamePet.Character-Limit.Enabled", false,
            """
                    Should the name have a limited number of characters?
                    
                    Default: {default}""");
    public final ConfigEntry<Integer> RENAME_LIMIT_CHARS_NUMBER = createOption("RenamePet.Character-Limit.Limit", 10,
            """
                    What should the character limit be set to?
                    
                    Default: {default}""");
    public final ConfigEntry<List<String>> RENAME_BLOCKED_WORDS = createOption("RenamePet.Blocked-Words", Lists.newArrayList("jeb_"),
            """
                    Are there words you don't want in a pets name?
                       If You put an ^ at the start of the word/text it will ignore case
                             Example: ^ass will also flag ASS because it will ignore what case the letters are
                       If you put word in between () it will check if it contains ANY part of the word
                             Example: (ass) will also flag glass because it contains the word in it
                       If you just have the word it will check for the exact word
                             Example: 'ass' will just be flagged so 'glass' can be said
                    
                    Default: {default}""");
    public final ConfigEntry<String> RENAME_BLOCKED_PATTERN = createOption("RenamePet.Blocked-RegexPattern", "",
            """
                    This blocks pets from having anything that matches the pattern as the name
                    For example MergedMobs has a pattern of '([0-9]+)(x)' will block names that are '999x'
                    If you want to use that pattern but ignore case then use '/([0-9]+)(x)/gmi'
                    ONLY CHECKS IF THE NAME MATCHES THE PATTERN
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> RENAME_COLOR_MAGIC = createOption("RenamePet.Allow-&k", false,
            """
                    Are pet names allowed to have the &k color code?
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> RENAME_COLOR_ENABLED = createOption("RenamePet.ColorCodes.Enabled", true,
            """
                    Are pet names allowed to be colored?
                    
                    Default: {default}""");
    public final ConfigEntry<Boolean> RENAME_COLOR_HEX = createOption("RenamePet.ColorCodes.Allow-HEX-Colors", true,
            """
                    Are pet names allowed to have HEX colors?
                    NOTE: If this is disabled only regular chat color will be used (EG: '&c')
                    Example: '&#c986b2'
                    
                    Default: {default}""");






    private <T> ConfigEntry<T> createOption(String key, T value, String description) {
        ConfigEntry<T> option = new ConfigEntry<>(key, value, description);
        options.put(key, option);
        return option;
    }
    private <T> ConfigEntry<T> createOption(String key, T value) {
        ConfigEntry<T> option = new ConfigEntry<>(key, value);
        options.put(key, option);
        return option;
    }

    public Map<String, ConfigEntry> getOptions() {
        return options;
    }
}
