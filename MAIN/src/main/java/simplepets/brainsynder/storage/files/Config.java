package simplepets.brainsynder.storage.files;

import org.bukkit.plugin.java.JavaPlugin;
import simplepets.brainsynder.storage.files.base.FileMaker;

import java.util.Arrays;
import java.util.Collections;

public class Config extends FileMaker {
    public Config(JavaPlugin plugin, String fileName) {
        super(plugin, fileName);
    }

    public void loadDefaults() {
        setDefault("Needs-Pet-Permission-To-Open-GUI", true);
        setDefault("Needs-Permission", true);
        setDefault("Remove-Item-If-No-Permission", true);
        setDefault("RemovePetsOnWorldChange", true);
        setDefault("UseVaultEconomy", false);
        setDefault("ShowParticles", true);
        setDefault("Complete-Mobspawning-Deny-Bypass", true);
        setDefault("Use&k", false);
        setDefault("ColorCodes", true);
        setDefault("PetItemStorage.Enable", true);
        setDefault("PetItemStorage.Inventory-Size", 27);
        setDefault("OldPetRegistering", false); // TODO: Might Remove
        setDefault("Needs-Data-Permissions", true); // TODO: Reformat this value
        setDefault("Pathfinding.Min-Distance-For-Teleport", 20.0);
        setDefault("Pathfinding.Stopping-Distance", 7.0);

        setDefault("Worlds.Enabled", false);
        setDefault("Worlds.Allowed-Worlds", Collections.singletonList("world"));

        setDefault("WorldGuard.BypassPermission", "region.bypass");
        setDefault("WorldGuard.Spawning.Always-Allowed", true);
        setDefault("WorldGuard.Spawning.Blocked-Regions", Collections.singletonList("pvp"));
        setDefault("WorldGuard.Pet-Entering.Always-Allowed", true);
        setDefault("WorldGuard.Pet-Entering.Blocked-Regions", Collections.singletonList("pvp"));
        setDefault("WorldGuard.Pet-Riding.Always-Allowed", true);
        setDefault("WorldGuard.Pet-Riding.Blocked-Regions", Collections.singletonList("pvp"));

        setDefault("PlotSquared.BypassPermission", "plots.admin");
        setDefault("PlotSquared.On-Unclaimed-Plots.Move", true);
        setDefault("PlotSquared.On-Unclaimed-Plots.Spawn", true);
        setDefault("PlotSquared.On-Unclaimed-Plots.Riding", true);
        setDefault("PlotSquared.On-Roads.Move", true);
        setDefault("PlotSquared.On-Roads.Spawn", true);
        setDefault("PlotSquared.On-Roads.Riding", true);
        setDefault("PlotSquared.Block-If-Denied.Move", true);
        setDefault("PlotSquared.Block-If-Denied.Spawn", true);
        setDefault("PlotSquared.Block-If-Denied.Riding", true);

        setDefault("WorldBorder.Block-If-Denied.Move", true);
        setDefault("WorldBorder.Block-If-Denied.Spawn", true);
        setDefault("WorldBorder.Block-If-Denied.Riding", true);

        setDefault("MySQL.Enabled", false);
        setDefault("MySQL.Host", "host");
        setDefault("MySQL.Port", "3306");
        setDefault("MySQL.DatabaseName", "insert_DatabaseName");
        setDefault("MySQL.Login.Username", "username");
        setDefault("MySQL.Login.Password", "password");
        setDefault("MySQL.Options.UseSSL", false);
        setDefault("MySQL.Options.AutoReconnect", false);

        setDefault("Debug.Enabled", false);
        setDefault("Debug.Levels", Arrays.asList("0", "1", "2"));

        // TODO: Reformat these value
        setDefault("PetToggles.GlowWhenVanished", true);
        setDefault("PetToggles.HideNameOnShift", true);
        setDefault("PetToggles.AutoRemove.Enabled", true);
        setDefault("PetToggles.AutoRemove.TickDelay", 10000);
        setDefault("Allow-Pets-Being-Mounts", true);
        setDefault("Allow-Pets-Being-Hats", true);
        setDefault("Respawn-Last-Pet-On-Login", true);

        setDefault("RenamePet.Enabled", true);
        setDefault("RenamePet.ViaAnvil", true);
        setDefault("RenamePet.Limit-Number-Of-Characters", false);
        setDefault("RenamePet.CharacterLimit", 10);
        setDefault("RenamePet.Blocked-Words", Arrays.asList("jeb_"));
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
