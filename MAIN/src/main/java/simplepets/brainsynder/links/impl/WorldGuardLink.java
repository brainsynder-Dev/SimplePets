package simplepets.brainsynder.links.impl;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.links.IWorldGuardLink;
import simplepets.brainsynder.links.impl.worldguard.WGInterface;
import simplepets.brainsynder.links.impl.worldguard.latest.WGLatest;
import simplepets.brainsynder.links.impl.worldguard.old.WGOld;
import simplepets.brainsynder.player.PetOwner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WorldGuardLink extends PluginLink implements IWorldGuardLink {
    private WGInterface wgInterface = null;
    public WorldGuardLink() {
        super("WorldGuard");
    }

    @Override
    public boolean onHook() {
        ClassLoader loader = PetCore.get().getLoader();
        try {
            Class.forName("com.sk89q.worldguard.bukkit.WorldGuardPlugin", false, loader);
            // Server uses old WorldGuard API
            PetCore.get().debug("Server is using the old WorldGuard API");
            wgInterface = new WGOld();
        }catch (Exception e) {
            try {
                Class.forName("com.sk89q.worldguard.WorldGuard", false, loader);
                // Server uses new WorldGuard API
                PetCore.get().debug("Server is using the new WorldGuard API");
                wgInterface = new WGLatest();
            }catch (Exception e2) {
                return false;
            }
        }

        if ( (wgInterface != null) && (!wgInterface.initiate()) ) {
            PetCore.get().debug("An Error occurred when initiating WGInterface");
            return false;
        }
        return true;
    }

    @Override
    public void onUnhook() {
        if (wgInterface != null) wgInterface.cleanup();
        wgInterface = null;
        super.onUnhook();
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
        ApplicableRegionSet regionSet = wgInterface.getRegionSet(at);
        Set<ProtectedRegion> regionIterator = regionSet.getRegions();
        List<String> regions = PetCore.get().getConfiguration().getStringList("WorldGuard."+section+".Blocked-Regions");
        List<Boolean> val = new ArrayList<>();
        for (ProtectedRegion rg : regionIterator)
            val.add(regions.contains(rg.getId()));
        return (!val.contains(true));
    }
}
