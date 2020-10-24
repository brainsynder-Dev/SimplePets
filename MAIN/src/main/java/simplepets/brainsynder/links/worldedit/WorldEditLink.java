package simplepets.brainsynder.links.worldedit;

import com.sk89q.worldedit.WorldEdit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.PetCore;

public class WorldEditLink {
    public static void init () {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldEdit");
        if (plugin == null) return;
        if (plugin.isEnabled()) {
            WorldEdit.getInstance().getEventBus().register(new WorldEditExtentHandler());

            PetCore.get().debug("WorldEdit was successfully linked");
        }
    }
}
