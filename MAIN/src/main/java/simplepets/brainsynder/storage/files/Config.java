package simplepets.brainsynder.storage.files;

import lib.brainsynder.utils.Utilities;
import org.bukkit.plugin.java.JavaPlugin;
import simplepets.brainsynder.storage.files.base.FileMaker;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class Config extends FileMaker {
    public Config(JavaPlugin plugin, String fileName) {
        super(plugin.getDataFolder(), fileName);
    }

    public void loadDefaults() {
        addDefault("Update-Checking.Enabled", true, "Would you like to check for new jenkins builds?\nDefault: true");
        addDefault("Update-Checking.Message-On-Join", true, "Would you like to be alerted when there is a new update when you log in?\n(MUST HAVE 'pet.update' permission or OP)\nDefault: true");
        addSectionHeader("Update-Checking.unit", Utilities.AlignText.LEFT, "The unit of time for update checking\nTime Units:\n- SECONDS\n- MINUTES\n- HOURS\n- DAYS");
        addDefault("Update-Checking.unit", TimeUnit.HOURS.name());
        addDefault("Update-Checking.time", 12);

        addDefault("Needs-Pet-Permission-To-Open-GUI", false, "Enabling this would require players to have access to at least 1 pets permission\nDefault: false");
        addDefault("Needs-Permission", true, "Disabling this would grant ALL players access to pets (they wont need permissions)\nDefault: true");
        addDefault("Needs-Data-Permissions", true, "Disabling this will make it so players do not need to have any data permissions (EG. pet.type.armorstand.data.silent)\nDefault: true"); // TODO: Reformat this value
        addDefault("Remove-Item-If-No-Permission", true, "Disabling this would remove all the pets the player does not have access to from the GUI\nDefault: true");

        addDefault("RemovePetsOnWorldChange", true, "Disabling this will remove a players pet when they change worlds\nDefault true");

        addDefault(ECONOMY_TOGGLE, false,
                "Enabling this would allow players to buy pets via Vault/TokenManager\n" +
                        "NOTE: If 'Needs-Permission' is set to true the players will still need the permission for the pet\n" +
                        "Default: false");
        addDefault(ECONOMY_TYPE, "UNKNOWN",
                "What type of economy do you have?\n" +
                        "Economy Types:\n" +
                        "- UNKNOWN (Will act like all pets are free)\n" +
                        "- EXPERIENCE_LEVEL (Will use the players experience levels as payment)\n" +
                        "- EXPERIENCE_POINTS (Will use the players experience points as payment [level 30 = 1395])\n" +
                        "- VAULT https://www.spigotmc.org/resources/34315/\n" +
                        "- TOKEN_MANAGER https://www.spigotmc.org/resources/8610/\n" +
                        "- GEMS_ECONOMY https://www.spigotmc.org/resources/19655/\n" +
                        "Default: 'UNKNOWN'");

        addDefault("ShowParticles", true, "Disabling this would make it so there is no particles when a player renames/removes/spawns a pet\nDefault: true");
        addDefault("Complete-Mobspawning-Deny-Bypass", true, "Disabling this would allow other plugins to deny the pets from spawning\nDefault: true");
        addDefault("PetItemStorage.Enable", true, "Disabling this will remove players access to a GUI that stores items\nDefault: true");
        addDefault("PetItemStorage.Inventory-Size", 27, "What size would you like the inventory to be?\nSizes: 9,18,27,36,45,54\nDefault: 27");
        addDefault("Pathfinding.Distance-to-Player", 1.9, "How far away can the pets stand near the player?\nDefault: 1.9");
        addDefault("Pathfinding.Distance-to-Player_LargePets", 2.9, "How far away can the large pets (Giants/Ghast) stand near the player?\nDefault: 2.9");
        addDefault("Pathfinding.Min-Distance-For-Teleport", 20.0, "How far away from the player does the pet have to be before it teleports closer?\nDefault: 20");
        addDefault("Pathfinding.Stopping-Distance", 3.0, "How far away can the pet be before it will stop walking near the player?\nDefault: 3");
        addDefault("Pathfinding.Stopping-Distance_LargePets", 7.0, "How far away can the large pet (Giant/Ghast) be before it will stop walking near the player?\nDefault: 7");

        addDefault("Worlds.Enabled", false, "Enabling this will make it so pets only work in the worlds that are listed in 'Allowed-Worlds'\nDefault: false");
        addDefault("Worlds.Allowed-Worlds", Collections.singletonList("world"));

        addSectionHeader("WorldGuard", Utilities.AlignText.LEFT, "Recently our code changed to support WorldGuard flags\nFlag Names:\n- allow-pet-spawn\n- allow-pet-enter\n- allow-pet-riding");
        addDefault("WorldGuard.BypassPermission", "region.bypass", "This is the bypass permission for WorldGuard\nDefault: region.bypass");

        addDefault("PlotSquared.BypassPermission", "plots.admin", "This is the bypass permission for PlotSquared\nDefault: plots.admin");
        addDefault("PlotSquared.On-Unclaimed-Plots.Move", true, "Are pets allowed to move on unclaimed plots?\nDefault: true");
        addDefault("PlotSquared.On-Unclaimed-Plots.Spawn", true, "Are pets allowed to be spawned on unclaimed plots?\nDefault: true");
        addDefault("PlotSquared.On-Unclaimed-Plots.Riding", true, "Are players allowed to ride pets on unclaimed plots?\nDefault: true");
        addDefault("PlotSquared.On-Roads.Move", true, "Are pets allowed to move on the roads?\nDefault: true");
        addDefault("PlotSquared.On-Roads.Spawn", true, "Are pets allowed to be spawned while on roads\nDefault: true");
        addDefault("PlotSquared.On-Roads.Riding", true, "Are players allowed to ride pets while on a road\nDefault: true");
        addDefault("PlotSquared.Block-If-Denied.Move", true, "Are pets allowed to move on a plot their owner is blocked on?\nDefault: true");
        addDefault("PlotSquared.Block-If-Denied.Spawn", true, "Are pets allowed to be spawned on plots their owner is blocked on?\n(Is this even needed? Its not like they can get on the plot anyway XD)\n\nDefault: true");
        addDefault("PlotSquared.Block-If-Denied.Riding", true, "Can player ride their pets onto plots they are denied on?\nDefault: true");

        addDefault("WorldBorder.Block-If-Denied.Move", true, "Are pets allowed to move when inside a WorldBorder?\nDefault: true");
        addDefault("WorldBorder.Block-If-Denied.Spawn", true, "Can pets be spawned in a WorldBorder?\nDefault: true");
        addDefault("WorldBorder.Block-If-Denied.Riding", true, "Can a player ride a pet in a WorldBorder?\nDefault: true");

        addDefault("MySQL.Enabled", false, "Would you like to use MySQL to save player/pet data?\nDefault: false");
        addDefault("MySQL.Host", "host");
        addDefault("MySQL.Port", "3306");
        addDefault("MySQL.DatabaseName", "insert_DatabaseName", "Example: SimplePets");
        addDefault("MySQL.Login.Username", "username");
        addDefault("MySQL.Login.Password", "password");
        addDefault("MySQL.Options.UseSSL", false);
        addDefault("MySQL.Options.AutoReconnect", false, "Enabling this will make the plugin attempt to reconnect to the database\nDefault: false");

        addDefault("Debug.Enabled", false, "Would you like to view Debug information in the console/logs?\nIt can help us see where issues are.\nDefault: false");
        addDefault("Debug.Levels", Arrays.asList("0", "1", "2"), "What level of debug info would you like to see?\n0 = Normal Info\n1 = Moderate Info (Warnings)\n2 = Critical/Errors (No explanation here i hope...)");

        // TODO: Reformat these value
        addDefault("PetToggles.GlowWhenVanished", true, "When the owner is vanished should the owner see their pet with the glow effect?\nDefault: true");
        addDefault("PetToggles.HideNameOnShift", true, "Should pet names be hidden when their owner sneaks?\nDefault: true");
        addDefault("PetToggles.AutoRemove.Enabled", true, "Disabling this will make it so pets wont be automatically removed if the player is afk\nDefault: true");
        addDefault("PetToggles.AutoRemove.TickDelay", 10000, "What should the wait be?\nThis is in ticks (20 ticks = 1 second)\nDefault: 10000");
        addDefault(MOUNTABLE, true, "Are all pets able to be rideable?\nDefault: true");
        addDefault(HATS, true, "Are all pets able to be worn as hats?\nDefault: true");
        addDefault("Respawn-Last-Pet-On-Login", true, "When a player logs back in should their pet be spawned in as well?\nNOTE: If the player removed their pet before logging out then it wont respawn.\nDefault: true");

        addDefault("RenamePet.Enabled", true, "Should players be able to rename pets?\nDefault: true");
        addDefault("RenamePet.ViaAnvil", true, "Should pet names be changed in an anvil?\ntrue = Anvil\nfalse = Chat\nDefault: true");
        addDefault("RenamePet.Limit-Number-Of-Characters", false, "Should the name have a limited number of characters?\nDefault: false");
        addDefault("RenamePet.CharacterLimit", 10, "What should the character limit be set to?\nDefault: 10");
        addDefault("RenamePet.Blocked-Words", Arrays.asList("jeb_"), "Are there words you don't want in a pets name?");
        addDefault("RenamePet.Blocked-RegexPattern", "", "This blocks pets from having anything that matches the pattern as the name\nFor example MergedMobs has a pattern of '([0-9]+)(x)' will block names that are '999x'\nONLY CHECKS IF THE NAME MATCHES THE PATTERN\nDefault: \"\"");
        addDefault(MAGIC, false, "Are pet names allowed to have the &k color code?\nDefault: false");
        addDefault(COLOR, true, "Are pet names allowed to be colored?\nDefault: true");
        updateSections();
    }

    private void updateSections () {
        move("Use&k", MAGIC, logMove());
        move("ColorCodes", COLOR, logMove());
        move("Allow-Pets-Being-Mounts", MOUNTABLE, logMove());
        move("Allow-Pets-Being-Hats", HATS, logMove());
        remove("WorldGuard.Spawning.Always-Allowed");
        remove("WorldGuard.Spawning.Blocked-Regions");
        remove("WorldGuard.Pet-Entering.Always-Allowed");
        remove("WorldGuard.Pet-Entering.Blocked-Regions");
        remove("WorldGuard.Pet-Riding.Always-Allowed");
        remove("WorldGuard.Pet-Riding.Blocked-Regions");
        remove("OldPetRegistering");
        move("UseVaultEconomy", ECONOMY_TOGGLE);

    }

    public static final String
            MOUNTABLE = "PetToggles.All-Pets-Mountable",
            HATS = "PetToggles.All-Pets-Hat",
            MAGIC = "RenamePet.Allow-&k",
            COLOR = "RenamePet.Allow-ColorCodes",
            ECONOMY_TOGGLE = "Economy.Enabled",
            ECONOMY_TYPE = "Economy.Type",
            NEEDS_PERM_GUI = "Economy.Type";
}
