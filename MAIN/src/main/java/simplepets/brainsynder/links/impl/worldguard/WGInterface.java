package simplepets.brainsynder.links.impl.worldguard;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Location;
import org.bukkit.World;

public interface WGInterface {
    default boolean initiate (){ return true; }
    default void cleanup (){}

    RegionManager getRegionManager(World world);

    ApplicableRegionSet getRegionSet(Location location);
}
