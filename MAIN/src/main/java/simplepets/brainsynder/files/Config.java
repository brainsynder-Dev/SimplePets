package simplepets.brainsynder.files;

import com.google.common.collect.Lists;
import lib.brainsynder.files.YamlFile;
import lib.brainsynder.utils.Utilities;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.utils.RenameType;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class Config extends YamlFile {
    public Config(PetCore core) {
        super(core.getDataFolder(), "config.yml");
    }

    @Override
    public void loadDefaults() {
        addDefault("Reload-Detected", false, "This is used by the plugin to detect if the plugin was previously unloaded (via /reload or by a plugin)\n    CAN NOT BE CUSTOMIZED");
        addDefault("Update-Checking.Enabled", true, "Would you like to check for new jenkins builds?\nDefault: true");
        addDefault("Update-Checking.Message-On-Join", true, "Would you like to be alerted when there is a new update when you log in?\n(MUST HAVE 'pet.update' permission or OP)\nDefault: true");
        addSectionHeader("Update-Checking.unit", Utilities.AlignText.LEFT, "The unit of time for update checking\nTime Units:\n- SECONDS\n- MINUTES\n- HOURS\n- DAYS");
        addDefault("Update-Checking.unit", TimeUnit.HOURS.name());
        addDefault("Update-Checking.time", 12);

        addDefault("Permissions.Ignored-List", Lists.newArrayList(
                ""
        ), "Any permission in this list will be ignored when the plugin checks if the player has permission\n(Will act like the player has the permission, without actually having it)");

        addDefault("Permissions.Needs-Pet-Permission-for-GUI", false, "Enabling this would require players to have access to at least 1 pets permission\nDefault: false");
        addDefault("Permissions.Enabled", true, "Disabling this would grant ALL players access to pets (they wont need permissions)\nDefault: true");
        addDefault("Permissions.Data-Permissions", true, "Disabling this will make it so players do not need to have any data permissions (EG. pet.type.armorstand.data.silent)\nDefault: true"); // TODO: Reformat this value
        addDefault("Permissions.Only-Show-Pets-Player-Can-Access", true, "Enabling this would remove all the pets the player does not have access to from the GUI\nDefault: true");

        addDefault("RemovePetsOnWorldChange", true, "Disabling this will remove a players pet when they change worlds\nDefault true");

        addDefault("Economy", "Now Separate",
                "Since v5 of SimplePets, Support for economy plugins for purchasing pets IN-Game\n" +
                        "has been split into different module for each economy supported...\n" +
                        "Why? mostly because it helps keep the code clean and if we ever need to fix issues\n" +
                        "due to an api change, it wont require us to update the whole plugin, instead we just update the addon.\n\n" +
                        "Any Questions contact us via Discord: https://pluginwiki.us/discord/"
        );
        addDefault(SPAWN_PARTICLE_TOGGLE, true, "Disabling this would make it so there is no particles when a player spawns a pet\nDefault: true");
        addDefault(FAILED_PARTICLE_TOGGLE, true, "Disabling this would make it so there is no particles when a task fails for a player (EG: spawning, riding, etc..)\nDefault: true");
        addDefault(REMOVE_PARTICLE_TOGGLE, true, "Disabling this would make it so there is no particles when a player removes a pet\nDefault: true");
        addDefault(NAME_PARTICLE_TOGGLE, true, "Disabling this would make it so there is no particles when a player renames a pet\nDefault: true");
        addDefault(TELEPORT_PARTICLE_TOGGLE, true, "Disabling this would make it so there is no particles when a players pet teleports to its owner\nDefault: true");
        addDefault(FAILED_TASK_PARTICLE_TOGGLE, true, "Disabling this would make it so there is no particles when a task for a pet fails\nDefault: true");

        addSectionHeader("PetItemStorage", "The ability to have pets store items in another inventory is temporarily not implemented yet\nThis feature will be back as either an addon or re-implemented into the plugin");
        addDefault("PetItemStorage.Enable", true, "Disabling this will remove players access to a GUI that stores items\nDefault: true");
        addDefault("PetItemStorage.Inventory-Size", 27, "What size would you like the inventory to be?\nSizes: 9,18,27,36,45,54\nDefault: 27");

        addDefault("Pathfinding.Distance-to-Player", 1.9, "How far away can the pets stand near the player?\nDefault: 1.9");
        addDefault("Pathfinding.Distance-to-Player_LargePets", 2.9, "How far away can the large pets (Giants/Ghast) stand near the player?\nDefault: 2.9");
        addDefault("Pathfinding.Min-Distance-For-Teleport", 20.0, "How far away from the player does the pet have to be before it teleports closer?\nDefault: 20");
        addDefault("Pathfinding.Stopping-Distance", 3.0, "How far away can the pet be before it will stop walking near the player?\nDefault: 3");
        addDefault("Pathfinding.Stopping-Distance_LargePets", 7.0, "How far away can the large pet (Giant/Ghast) be before it will stop walking near the player?\nDefault: 7");

        addSectionHeader("Worlds", "Currently not implemented, but will be re-implemented in the future");
        addDefault("Worlds.Enabled", false, "Enabling this will make it so pets only work in the worlds that are listed in 'Allowed-Worlds'\nDefault: false");
        addDefault("Worlds.Allowed-Worlds", Collections.singletonList("world"));

        addSectionHeader("WorldGuard", Utilities.AlignText.LEFT, "Recently our code changed to support WorldGuard flags\nFlag Names:\n- allow-pet-spawn\n- allow-pet-enter\n- allow-pet-riding");
        addDefault("WorldGuard.BypassPermission", "region.bypass", "This is the bypass permission for WorldGuard\nDefault: region.bypass");

        remove("PlotSquared.BypassPermission");
        remove("PlotSquared.On-Unclaimed-Plots.Move");
        remove("PlotSquared.On-Unclaimed-Plots.Spawn");
        remove("PlotSquared.On-Unclaimed-Plots.Riding");
        remove("PlotSquared.On-Roads.Move");
        remove("PlotSquared.On-Roads.Spawn");
        remove("PlotSquared.On-Roads.Riding");
        remove("PlotSquared.Block-If-Denied.Move");
        remove("PlotSquared.Block-If-Denied.Spawn");
        remove("PlotSquared.Block-If-Denied.Riding");
        addDefault("WorldBorder.Block-If-Denied.Move", true, "Are pets allowed to move when inside a WorldBorder?\nDefault: true");
        addDefault("WorldBorder.Block-If-Denied.Spawn", true, "Can pets be spawned in a WorldBorder?\nDefault: true");
        addDefault("WorldBorder.Block-If-Denied.Riding", true, "Can a player ride a pet in a WorldBorder?\nDefault: true");

        addDefault("MySQL.Enabled", false, "Would you like to use MySQL to save player/pet data?\nIf it is disabled the plugin will use SQLite\nDefault: false");
        addDefault("MySQL.Table", "simplepets");
        addDefault("MySQL.Host", "localhost");
        addDefault("MySQL.Port", 3306);
        addDefault("MySQL.DatabaseName", "SimplePets", "Example: SimplePets");
        addDefault("MySQL.Login.Username", "username");
        addDefault("MySQL.Login.Password", "password");
        addDefault("MySQL.Options.UseSSL", false);

        addDefault("Debug.Enabled", true, "Would you like to view Debug information in the console/logs?\nIt can help us see where issues are.\nDefault: true");
        addComment("Debug.Levels", "What level of debug info would you like to see?\n" +
                "NORMAL = Normal Info\n" +
                "WARNING = Used for warnings like a missing bit of info (but it has a default it can use)\n" +
                "ERROR = Critical/Errors (No explanation here i hope...)");
        addDefault("Debug.Levels.NORMAL", true);
        addDefault("Debug.Levels.WARNING", true);
        addDefault("Debug.Levels.ERROR", true);

        // TODO: Reformat these value
        addDefault("PetToggles.MobSpawnBypass", true,
                "When this is enabled it will allow pets to spawn anywhere\n" +
                "This is especially used if WorldGuard/PlotSquared are blocking mobs from spawning\nDefault: true");
        addDefault(PUSH_PETS, false, "Are pets able to be pushed around?\nDefault: false");
        addDefault("PetToggles.GlowWhenVanished", true, "When the owner is vanished should the owner see their pet with the glow effect?\nDefault: true");
        addDefault("PetToggles.ShowPetNames", true, "Should pet names be seen?\nDefault: true");
        addDefault("PetToggles.HideNameOnShift", true, "Should pet names be hidden when their owner sneaks?\nDefault: true");
        addDefault("PetToggles.AutoRemove.Enabled", true, "Disabling this will make it so pets wont be automatically removed if the player is afk\nDefault: true");
        addDefault("PetToggles.AutoRemove.TickDelay", 10000, "What should the wait be?\nThis is in ticks (20 ticks = 1 second)\nDefault: 10000");
        addDefault(MOUNTABLE, true, "Are all pets able to be rideable?\nDefault: true");
        addDefault(HATS, true, "Are all pets able to be worn as hats?\nDefault: true");
        addDefault(FLYABLE, false, "Are all pets able to fly when mounted?\nDefault: false");
        addDefault("PetToggles.Default-Spawn-Limit", 3, "The maximum number of pets a player can spawn at a time.\n" +
                "This can be overridden using pet.amount.<Number>, e.g. pet.amount.1 to only allow 1 at once.\n" +
                "Default: 3");
        addDefault("PetToggles.Default-Walk-Speed", 0.6000000238418579, "The default walk speed each pet will go at.\n" +
                "This can be overriden in an individual pet .json file using the walk_speed key.");
        addDefault("PetToggles.Default-Ride-Speed", 0.4000000238418579, "The default ride speed each pet will go at when mounted.\n" +
                "This can be overriden in an individual pet .json file using the ride_speed key.");
        addDefault("Respawn-Last-Pet-On-Login", true, "When a player logs back in should their pet be spawned in as well?\nNOTE: If the player removed their pet before logging out then it wont respawn.\nDefault: true");

        addDefault("RenamePet.Enabled", true, "Should players be able to rename pets?\nDefault: true");
        addDefault("RenamePet.Type", RenameType.ANVIL.name(), "How should the player be able to modify their pets name?\n" +
                "Types:\n" +
                "- COMMAND (They have to use the '/pet rename' command to set the name)\n" +
                "- CHAT (They have to type the name in chat)\n" +
                "- ANVIL (The Anvil GUI will open and they can change the name there)\n" +
                "- SIGN [REQUIRES ProtocolLib] (Will open a Sign GUI they input the name on the configured line)\n" +
                "Default: ANVIL");
        addDefault(RENAME_TRIM, false, "Enabling this option will remove random spaces at the start and end of the name");
        addDefault(LIMIT_CHARS_TOGGLE, false, "Should the name have a limited number of characters?\nDefault: false");
        addDefault(LIMIT_CHARS_TOGGLE, false, "Should the name have a limited number of characters?\nDefault: false");
        addDefault(LIMIT_CHARS_NUMBER, 10, "What should the character limit be set to?\nDefault: 10");
        addDefault("RenamePet.Blocked-Words", Arrays.asList("jeb_"),
                "Are there words you don't want in a pets name?\n" +
                        "   If you put word in between [] it will check if it contains ANY part of the word\n" +
                        "         Example: [ass] will also flag glass because it contains the word in it\n" +
                        "   If you just have the word it will check for the exact word\n" +
                        "         Example: 'ass' will just be flagged so 'glass' can be said");
        addDefault("RenamePet.Blocked-RegexPattern", "", "This blocks pets from having anything that matches the pattern as the name\nFor example MergedMobs has a pattern of '([0-9]+)(x)' will block names that are '999x'\nIf you want to use that pattern but ignore case then use '/([0-9]+)(x)/gmi'\nONLY CHECKS IF THE NAME MATCHES THE PATTERN\nDefault: \"\"");
        addDefault(MAGIC, false, "Are pet names allowed to have the &k color code?\nDefault: false");
        addDefault(COLOR, true, "Are pet names allowed to be colored?\nDefault: true");
        addDefault(HEX, true, "Are pet names allowed to have HEX colors?\nNOTE: If this is disabled only regular chat color will be used (EG: '&c')\nExample: '&#c986b2'\nDefault: true");
        updateSections();
    }


    public void setEnum(String key, Enum anEnum) {
        set(key, anEnum.name());
    }

    public <E extends Enum>E getEnum (String key, Class<E> type) {
        return getEnum(key, type, null);
    }
    public <E extends Enum>E getEnum (String key, Class<E> type, E fallback) {
        if (!contains(key)) return fallback;
        return (E) E.valueOf(type, getString(key));
    }

    private void updateSections () {
        move("Needs-Pet-Permission-To-Open-GUI", "Permissions.Needs-Pet-Permission-for-GUI", logMove());
        move("Needs-Data-Permissions", "Permissions.Data-Permissions", logMove());
        move("Needs-Permission", "Permissions.Enabled", logMove());
        move("Remove-Item-If-No-Permission", "Permissions.Only-Show-Pets-Player-Can-Access", logMove());
        move("Use&k", MAGIC, logMove());
        move("ShowParticles", SPAWN_PARTICLE_TOGGLE, logMove());
        move("ColorCodes", COLOR, logMove());
        move("RenamePet.Limit-Number-Of-Characters", LIMIT_CHARS_TOGGLE, logMove());
        move("RenamePet.CharacterLimit", LIMIT_CHARS_NUMBER, logMove());
        move("RenamePet.Allow-ColorCodes", COLOR, logMove());
        move("Allow-Pets-Being-Mounts", MOUNTABLE, logMove());
        move("Allow-Pets-Being-Hats", HATS, logMove());
        remove("WorldGuard.Spawning.Always-Allowed");
        remove("WorldGuard.Spawning.Blocked-Regions");
        remove("WorldGuard.Pet-Entering.Always-Allowed");
        remove("WorldGuard.Pet-Entering.Blocked-Regions");
        remove("WorldGuard.Pet-Riding.Always-Allowed");
        remove("WorldGuard.Pet-Riding.Blocked-Regions");
        remove("OldPetRegistering");
        remove("MySQL.Options.Auto_Reconnect"); // Not needed as the new system needs the connection to remain
        remove("PetToggles.Weight.Enabled");
        remove("PetToggles.Weight.Weight_Stacked");
        remove("PetToggles.Weight.Max_Weight");
        remove("Complete-Mobspawning-Deny-Bypass"); // This has not been fully used since 1.8
        move("UseVaultEconomy", ECONOMY_TOGGLE);
        move("Particles.Failed-Summon", FAILED_PARTICLE_TOGGLE);

        move(ECONOMY_TOGGLE, "Economy");
        remove(ECONOMY_TYPE);

    }

    public static final String
            PUSH_PETS = "PetToggles.Move-Pets-Get-Out-Da-Way",
            MOUNTABLE = "PetToggles.All-Pets-Mountable",
            HATS = "PetToggles.All-Pets-Hat",
            FLYABLE = "PetToggles.All-Pets-Flyable",
            MAGIC = "RenamePet.Allow-&k",
            COLOR = "RenamePet.ColorCodes.Enabled",
            HEX = "RenamePet.ColorCodes.Allow-HEX-Colors",
            ECONOMY_TOGGLE = "Economy.Enabled",
            ECONOMY_TYPE = "Economy.Type",
            RENAME_TRIM = "RenamePet.Trim-Name",
            LIMIT_CHARS_TOGGLE = "RenamePet.Character-Limit.Enabled",
            LIMIT_CHARS_NUMBER = "RenamePet.Character-Limit.Limit",
            SPAWN_PARTICLE_TOGGLE = "Particles.Summon",
            FAILED_PARTICLE_TOGGLE = "Particles.Failed",
            TELEPORT_PARTICLE_TOGGLE = "Particles.Teleport",
            FAILED_TASK_PARTICLE_TOGGLE = "Particles.Failed-Task",
            REMOVE_PARTICLE_TOGGLE = "Particles.Remove",
            NAME_PARTICLE_TOGGLE = "Particles.Name-Change";


    protected BiConsumer<String, String> logMove () {
        return (oldKey, newKey) -> {
            String name = getClass().getSimpleName().replace("File", "");
            SimplePets.getDebugLogger().debug("["+name+"] Moving '"+oldKey+"' to '"+newKey+"'");
        };
    }
}
