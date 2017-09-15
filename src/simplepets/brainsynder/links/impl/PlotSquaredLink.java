package simplepets.brainsynder.links.impl;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotArea;
import com.plotsquared.bukkit.BukkitMain;
import com.plotsquared.bukkit.util.BukkitUtil;
import org.bukkit.Location;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.links.IPlotSquaredLink;

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
    public boolean allowPetEntry(Location at) {
        if (!isHooked()) return true;
        com.intellectualcrafters.plot.object.Location loc = BukkitUtil.getLocation(at);
        PlotArea area = loc.getPlotArea();
        if (area == null) return true;
        Plot now = area.getPlot(loc);
        if (now == null) return PetCore.get().getConfiguration().getBoolean("PlotSquared.Allow-Pets.On-Roads");
        if (!now.hasOwner())
            return PetCore.get().getConfiguration().getBoolean("PlotSquared.Allow-Pets.On-Unclaimed-Plots");
        return true;
    }

    @Override
    public boolean allowPetSpawn(Location at) {
        if (!isHooked()) return true;
        com.intellectualcrafters.plot.object.Location loc = BukkitUtil.getLocation(at);
        PlotArea area = loc.getPlotArea();
        if (area == null) return true;
        Plot now = area.getPlot(loc);
        if (now == null) return PetCore.get().getConfiguration().getBoolean("PlotSquared.Spawn-Pets.On-Roads");
        if (!now.hasOwner())
            return PetCore.get().getConfiguration().getBoolean("PlotSquared.Spawn-Pets.On-Unclaimed-Plots");
        return true;
    }
}
