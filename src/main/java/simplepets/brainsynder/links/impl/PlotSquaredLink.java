package simplepets.brainsynder.links.impl;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotArea;
import org.bukkit.Location;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.links.IPlotSquaredLink;
import simplepets.brainsynder.player.PetOwner;

public class PlotSquaredLink extends PluginLink implements IPlotSquaredLink {
    public PlotSquaredLink() {
        super("PlotSquared");
    }

    @Override public void onHook() {}
    @Override public void onUnhook() {}

    @Override
    public boolean allowPetEntry(PetOwner owner, Location at) {
        return fetchValue("Move", owner, at);
    }

    @Override
    public boolean allowPetSpawn(PetOwner owner, Location at) {
        return fetchValue("Spawn", owner, at);
    }

    @Override
    public boolean allowPetRiding(PetOwner owner, Location at) {
        return fetchValue("Riding", owner, at);
    }

    @Override
    public boolean allowPetEntry(Location location) {
        return allowPetEntry (null, location);
    }

    @Override
    public boolean allowPetSpawn(Location at) {
        return allowPetSpawn(null, at);
    }

    private boolean fetchValue (String section, PetOwner owner, Location at) {
        if (!isHooked()) return true;
        PS ps = PS.get();
        com.intellectualcrafters.plot.object.Location loc = new com.intellectualcrafters.plot.object.Location(at.getWorld().getName(), at.getBlockX(), at.getBlockY(), at.getBlockZ());
        PlotArea area = ps.getApplicablePlotArea(loc);
        if (area == null) return true;
        Plot plot = area.getPlot(loc);

        if (owner != null) {
            if (owner.getPlayer().hasPermission(PetCore.get().getConfiguration().getString("PlotSquared.BypassPermission", false))) return true;
        }

        if (plot == null) {
            return PetCore.get().getConfiguration().getBoolean("PlotSquared.On-Roads."+section);
        }

        if (!plot.hasOwner())
            return PetCore.get().getConfiguration().getBoolean("PlotSquared.On-Unclaimed-Plots."+section);

        if (PetCore.get().getConfiguration().getBoolean("PlotSquared.Block-If-Denied."+section) && (owner != null))
            return !plot.isDenied(owner.getPlayer().getUniqueId());

        return true;
    }
}
