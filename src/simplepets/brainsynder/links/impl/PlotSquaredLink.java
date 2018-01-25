package simplepets.brainsynder.links.impl;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotArea;
import com.plotsquared.bukkit.BukkitMain;
import com.plotsquared.bukkit.util.BukkitUtil;
import org.bukkit.Location;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.links.IPlotSquaredLink;
import simplepets.brainsynder.player.PetOwner;

public class PlotSquaredLink extends PluginLink<BukkitMain> implements IPlotSquaredLink {
    public PlotSquaredLink() {
        super("PlotSquared");
    }

    @Override
    public void onHook() {
    }

    @Override
    public void onUnhook() {
    }

    @Override
    public boolean allowPetEntry(PetOwner owner, Location at) {
        if (!isHooked()) return true;
        com.intellectualcrafters.plot.object.Location loc = BukkitUtil.getLocation(at);
        PlotArea area = loc.getPlotArea();
        if (area == null) return true;
        Plot plot = area.getPlot(loc);
        if (plot == null) return PetCore.get().getConfiguration().getBoolean("PlotSquared.Allow-Pets.On-Roads");
        if (!plot.hasOwner())
            return PetCore.get().getConfiguration().getBoolean("PlotSquared.Allow-Pets.On-Unclaimed-Plots");

        if (PetCore.get().getConfiguration().getBoolean("PlotSquared.Block-If-Denied"))
            if (owner != null) if (plot.isDenied(owner.getPlayer().getUniqueId())) return false;

        return true;
    }

    @Override
    public boolean allowPetSpawn(PetOwner owner, Location at) {
        if (!isHooked()) return true;
        com.intellectualcrafters.plot.object.Location loc = BukkitUtil.getLocation(at);
        PlotArea area = loc.getPlotArea();
        if (area == null) return true;
        Plot plot = area.getPlot(loc);
        if (plot == null) return PetCore.get().getConfiguration().getBoolean("PlotSquared.Spawn-Pets.On-Roads");
        if (!plot.hasOwner())
            return PetCore.get().getConfiguration().getBoolean("PlotSquared.Spawn-Pets.On-Unclaimed-Plots");

        if (PetCore.get().getConfiguration().getBoolean("PlotSquared.Block-If-Denied"))
            if (owner != null) if (plot.isDenied(owner.getPlayer().getUniqueId())) return false;

        return true;
    }

    @Override
    public boolean allowPetEntry(Location location) {
        if (!isHooked()) return true;
        com.intellectualcrafters.plot.object.Location loc = BukkitUtil.getLocation(location);
        PlotArea area = loc.getPlotArea();
        if (area == null) return true;
        Plot plot = area.getPlot(loc);
        if (plot == null) return PetCore.get().getConfiguration().getBoolean("PlotSquared.Allow-Pets.On-Roads");
        if (!plot.hasOwner())
            return PetCore.get().getConfiguration().getBoolean("PlotSquared.Allow-Pets.On-Unclaimed-Plots");
        return true;

    }

    @Override
    public boolean allowPetSpawn(Location at) {
        if (!isHooked()) return true;
        com.intellectualcrafters.plot.object.Location loc = BukkitUtil.getLocation(at);
        PlotArea area = loc.getPlotArea();
        if (area == null) return true;
        Plot plot = area.getPlot(loc);
        if (plot == null) return PetCore.get().getConfiguration().getBoolean("PlotSquared.Spawn-Pets.On-Roads");
        if (!plot.hasOwner())
            return PetCore.get().getConfiguration().getBoolean("PlotSquared.Spawn-Pets.On-Unclaimed-Plots");
        return true;
    }
}
