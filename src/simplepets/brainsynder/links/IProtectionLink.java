package simplepets.brainsynder.links;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public interface IProtectionLink<T extends Plugin> extends IPluginLink<T> {
    boolean allowPetEntry(Location location);

    boolean allowPetSpawn(Location at);
}