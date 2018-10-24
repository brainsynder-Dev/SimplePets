package simplepets.brainsynder.links.impl;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.links.IWorldGuardLink;
import simplepets.brainsynder.player.PetOwner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WorldGuardLink extends PluginLink<WorldGuardPlugin> implements IWorldGuardLink {
    public WorldGuardLink() {
        super("WorldGuard");
    }

    @Override
    public void onHook() {
    }

    @Override
    public void onUnhook() {
    }

    @Override
    public boolean allowPetEntry(PetOwner owner, Location at) {
        return fetchValue("Pet-Entering", owner, at);
    }

    @Override
    public boolean allowPetSpawn(PetOwner owner, Location at) {
        return fetchValue("Spawning", owner, at);
    }

    @Override
    public boolean allowPetRiding(PetOwner owner, Location at) {
        return fetchValue("Pet-Riding", owner, at);
    }

    private boolean fetchValue (String section, PetOwner owner, Location at) {
        if (!isHooked()) return true;
        if (PetCore.get().getConfiguration().getBoolean("WorldGuard."+section+".Always-Allowed")) return true;
        if (owner != null) {
            if (owner.getPlayer().hasPermission(PetCore.get().getConfiguration().getString("WorldGuard.BypassPermission", false)))
                return true;
        }
        RegionManager set = getDependency().getRegionManager(at.getWorld());
        ApplicableRegionSet regionSet = set.getApplicableRegions(at);
        Set<ProtectedRegion> regionIterator = regionSet.getRegions();
        List<String> regions = PetCore.get().getConfiguration().getStringList("WorldGuard."+section+".Blocked-Regions");
        List<Boolean> val = new ArrayList<>();
        for (ProtectedRegion rg : regionIterator) {
            val.add((!regions.contains(rg.getId())));
        }
        return (!val.contains(false));
    }
}
