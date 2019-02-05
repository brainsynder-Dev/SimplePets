package simplepets.brainsynder.links.impl.worldguard.latest;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.World;
import simplepets.brainsynder.links.impl.worldguard.WGInterface;

public class WGLatest implements WGInterface {
    private RegionManager manager;

    @Override
    public void cleanup() {
        manager = null;
    }

    @Override
    public RegionManager getRegionManager(World world) {
        if (manager != null) return manager;
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        manager = container.get(new BukkitWorld(world));
        return manager;
    }

    @Override
    public ApplicableRegionSet getRegionSet(Location loc) {
        return getRegionManager(loc.getWorld()).getApplicableRegions(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()));
    }
}
