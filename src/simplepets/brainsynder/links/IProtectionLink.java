package simplepets.brainsynder.links;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.player.PetOwner;

public interface IProtectionLink<T extends Plugin> extends IPluginLink<T> {
    default boolean allowPetEntry(PetOwner owner, Location location) { return allowPetEntry(location); }

    default boolean allowPetSpawn(PetOwner owner, Location at) { return allowPetSpawn(at); }

    default boolean allowPetEntry(Location location) { return allowPetEntry(null, location); }

    default boolean allowPetSpawn(Location at) { return allowPetSpawn(null, at); }
}