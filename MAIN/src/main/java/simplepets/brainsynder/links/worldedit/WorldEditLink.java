package simplepets.brainsynder.links.worldedit;

import com.sk89q.worldedit.WorldEdit;
import lib.brainsynder.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.PetCore;

public class WorldEditLink {
    public static void init () {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldEdit");
        if (plugin == null) return;
        if (plugin.isEnabled()) {
            if (!(ServerVersion.getVersion() == ServerVersion.v1_13_R2 || ServerVersion.getVersion() == ServerVersion.v1_13_R1)) {
                WorldEdit.getInstance().getEventBus().register(new WorldEditExtentHandler());
            }
            PetCore.get().debug("WorldEdit was successfully linked");
        }
    }
}
