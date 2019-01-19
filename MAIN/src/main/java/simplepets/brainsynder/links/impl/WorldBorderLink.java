package simplepets.brainsynder.links.impl;

import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.Config;
import com.wimbli.WorldBorder.WorldBorder;
import org.bukkit.Location;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.links.IWorldBorderLink;
import simplepets.brainsynder.player.PetOwner;

public class WorldBorderLink extends PluginLink<WorldBorder> implements IWorldBorderLink {
    public WorldBorderLink() {
        super("WorldBorder");
    }

    @Override
    public boolean onHook() {
        return true;
    }

    @Override
    public void onUnhook() {

    }

    @Override
    public boolean allowPetEntry(PetOwner owner, Location at) {
        return fetchValue("Move", at);
    }

    @Override
    public boolean allowPetSpawn(PetOwner owner, Location at) {
        return fetchValue("Spawn", at);
    }

    @Override
    public boolean allowPetRiding(PetOwner owner, Location at) {
        return fetchValue("Riding", at);
    }


    public boolean fetchValue(String section, Location loc) {
        if (!isHooked()) return true;
        if (!PetCore.get().getConfiguration().getBoolean("WorldBorder.Block-If-Denied." + section)) return true;
        BorderData border = Config.Border(loc.getWorld().getName());
        if (border == null) return true;
        return border.insideBorder(loc);
    }
}
