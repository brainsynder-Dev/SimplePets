package simplepets.brainsynder.files;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;

public class Config extends FileMaker {
    public Config(JavaPlugin plugin, String fileName) {
        super(plugin, fileName);
    }

    public void loadDefaults() {
        if (!isSet("PlayerPetNaming"))
            set("PlayerPetNaming", true, "Enable or Disable Pet Renaming");
        if (!isSet("Needs-Permission"))
            set("Needs-Permission", true, "Enable or Disable if Permissions are required");
        if (!isSet("Remove-Item-If-No-Permission"))
            set("Remove-Item-If-No-Permission", true, "Remove the item from the GUI", "If they don't have permission to the pet");
        if (!isSet("RemovePetsOnWorldChange"))
            set("RemovePetsOnWorldChange", true, "Should pets be removed when their owner", "Changes what world they are in");
        if (!isSet("UseVaultEconomy"))
            set("UseVaultEconomy", false, "Can players buy pets using the Vault API");
        if (!isSet("ShowParticles"))
            set("ShowParticles", true, "Enable or Disable particles from pets");
        if (!isSet("Complete-Mobspawning-Deny-Bypass"))
            set("Complete-Mobspawning-Deny-Bypass", true, "Override all denied mob spawning", "This also Overrides ALL WorldGuard Regions");
        if (!isSet("Use&k"))
            set("Use&k", false, "Should players be allowed to use the &k", "ColorCode in their pets name");
        if (!isSet("ColorCodes"))
            set("ColorCodes", true, "Should players be allowed to use ColorCodes in pet names");
        if (!isSet("PetItemStorage.Enable"))
            set("PetItemStorage.Enable", true, "Can players Store items with the Pets plugin?");
        if (!isSet("OldPetRegistering"))
            set("OldPetRegistering", false, "(Might be removed in a future version)", "Toggles weather the plugin uses the default Vanilla registering", "Or the Custom one");
        if (!isSet("AvailableSlots"))
            set("AvailableSlots", Arrays.asList(
                    "11", "12", "13", "14", "15", "16", "17",
                    "20", "21", "22", "23", "24", "25", "26",
                    "29", "30", "31", "32", "33", "34", "35",
                    "38", "39", "40", "41", "42", "43", "44"
            ), "Inventory slots where Pet items will be shown");
        if (!isSet("PetDataMenu-Enabled"))
            set("PetDataMenu-Enabled", true, "Does the Data menu show up", "The menu when Right-Clicking the pet");
        if (!isSet("Needs-Data-Permissions"))
            set("Needs-Data-Permissions", false, "Does the player need to have the", "data permissions for the pet?", "Needs-Permission overrides this value");
        if (!isSet("Allow-Pets-Being-Mounts"))
            set("Allow-Pets-Being-Mounts", true, "Does the pet mounting item show in the menu?");
        if (!isSet("Worlds.Enabled"))
            set("Worlds.Enabled", false, "Should pet spawning only be for certain worlds?");
        if (!isSet("Worlds.Allowed-Worlds"))
            set("Worlds.Allowed-Worlds", Collections.singletonList(
                    "world"
            ));
        if (!isSet("Allow-Pets-Being-Hats"))
            set("Allow-Pets-Being-Hats", true, "Does the pet hat item show in the menu?");
        if (!isSet("Respawn-Last-Pet-On-Login"))
            set("Respawn-Last-Pet-On-Login", true, "If the player had a pet when they logged out", "Should that pet be re-spawned on Login");
        if (!isSet("WorldGuard.Spawning.Always-Allowed"))
            set("WorldGuard.Spawning.Always-Allowed", true);
        if (!isSet("WorldGuard.Spawning.Blocked-Regions"))
            set("WorldGuard.Spawning.Blocked-Regions", Collections.singletonList("pvp"));
        if (!isSet("WorldGuard.Pet-Entering.Always-Allowed")) set("WorldGuard.Pet-Entering.Always-Allowed", true);
        if (!isSet("WorldGuard.Pet-Entering.Blocked-Regions"))
            set("WorldGuard.Pet-Entering.Blocked-Regions", Collections.singletonList("pvp"));

        if (!isSet("PlotSquared.Allow-Pets.On-Roads")) set("PlotSquared.Allow-Pets.On-Roads", true,
                "Allow-Pets On-Roads = Are pets allowed", "on roads in the PlotWorld?",
                "O__O",
                "Allow-Pets On-Unclaimed-Plots = Are pets allowed on", "unclaimed plots in the PlotWorld?",
                "O__O",
                "Spawn-Pets On-Roads = Are pets allowed to", "spawn on roads in the PlotWorld?",
                "O__O",
                "Spawn-Pets On-Unclaimed-Plots Are pets allowed to", "spawn on Unclaimed Plots in the PlotWorld?"
        );
        if (!isSet("PlotSquared.Allow-Pets.On-Unclaimed-Plots")) set("PlotSquared.Allow-Pets.On-Unclaimed-Plots", true);
        if (!isSet("PlotSquared.Spawn-Pets.On-Roads")) set("PlotSquared.Allow-Pets.On-Roads", true);
        if (!isSet("PlotSquared.Spawn-Pets.On-Unclaimed-Plots")) set("PlotSquared.Allow-Pets.On-Unclaimed-Plots", true);
        if (!isSet("PlotSquared.Block-If-Denied")) set("PlotSquared.Block-If-Denied", true);

        if (!isSet("MySQL.Enabled")) set("MySQL.Enabled", false,
                "MySQL support is still in beta", "if you find issues report them to ", "http://pluginwiki.tk/issues/");
        if (!isSet("MySQL.Host")) set("MySQL.Host", "host");
        if (!isSet("MySQL.Port")) set("MySQL.Port", "3306");
        if (!isSet("MySQL.DatabaseName")) set("MySQL.DatabaseName", "insert_DatabaseName");
        if (!isSet("MySQL.Login.Username")) set("MySQL.Login.Username", "username");
        if (!isSet("MySQL.Login.Password")) set("MySQL.Login.Password", "password");
        if (!isSet("MySQL.Options.UseSSL")) set("MySQL.Options.UseSSL", false);
        if (!isSet("MySQL.Options.AutoReconnect")) set("MySQL.Options.AutoReconnect", false);

        if (!isSet("Debug.Enabled"))
            set("Debug.Enabled", false,
                    "Should debug messages be sent to the Console?",
                    "These are PERFECT for finding where a problem is happening",
                    "(Would be nice if you have it on when reporting issues)", " ",
                    "The Levels are as follows ->", "0 = Regular", "1 = Warning", "2 = CRITICAL");
        if (!isSet("Debug.Levels"))
            set("Debug.Levels", Arrays.asList("0", "1", "2"));

        if (!isSet("PetToggles.GlowWhenVanished"))
            set("PetToggles.GlowWhenVanished", true);
        if (!isSet("PetToggles.HideNameOnShift"))
            set("PetToggles.HideNameOnShift", true);
        if (!isSet("PetToggles.AutoRemove.Enabled"))
            set("PetToggles.AutoRemove.Enabled", true);
        if (!isSet("PetToggles.AutoRemove.TickDelay"))
            set("PetToggles.AutoRemove.TickDelay", 10000);
        if (!isSet("PetToggles.Rename.ViaAnvil"))
            set("PetToggles.Rename.ViaAnvil", true);
        if (!isSet("PetToggles.Rename.Limit-Number-Of-Characters"))
            set("PetToggles.Rename.Limit-Number-Of-Characters", false);
        if (!isSet("PetToggles.Rename.CharacterLimit"))
            set("PetToggles.Rename.CharacterLimit", 10);
        if (!isSet("PetToggles.Rename.Blocked-Words"))
            set("PetToggles.Rename.Blocked-Words", Arrays.asList("jeb_"));
    }

    @Override
    public void set(String tag, Object data, String... comments) {
        try {
            super.set(tag, data);
        } catch (Exception e) {
            super.set(tag, data);
        }
    }
}
