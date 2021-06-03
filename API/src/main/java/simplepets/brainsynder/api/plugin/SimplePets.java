package simplepets.brainsynder.api.plugin;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Entity;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.inventory.handler.GUIHandler;
import simplepets.brainsynder.api.inventory.handler.ItemHandler;
import simplepets.brainsynder.api.other.ParticleHandler;
import simplepets.brainsynder.api.pet.PetConfigManager;
import simplepets.brainsynder.api.user.UserManagement;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.debug.DebugLogger;

public final class SimplePets {
    public static final DebugLevel ADDON;

    static {
        ADDON = new DebugLevel("ADDON", ChatColor.of("#dde6ea"), ChatColor.of("#a6f2d8"));
    }

    private static IPetsPlugin PLUGIN;

    public static void setPLUGIN(IPetsPlugin plugin) {
        if(PLUGIN != null) return;
        PLUGIN = plugin;
    }

    public static IPetsPlugin getPlugin() {
        return PLUGIN;
    }

    public static UserManagement getUserManager () {
        return PLUGIN.getUserManager();
    }

    public static ISpawnUtil getSpawnUtil () {
        return PLUGIN.getSpawnUtil();
    }

    public static PetConfigManager getPetConfigManager () {
        return PLUGIN.getPetConfigManager();
    }

    public static ItemHandler getItemHandler () {
        return PLUGIN.getItemHandler();
    }

    public static GUIHandler getGUIHandler () {
        return PLUGIN.getGUIHandler();
    }

    public static ParticleHandler getParticleHandler () {
        return PLUGIN.getParticleHandler();
    }

    public static DebugLogger getDebugLogger () {
        return PLUGIN.getDebugLogger();
    }

    public static boolean isPetEntity (Entity entity) {
        return PLUGIN.isPetEntity(entity);
    }
}
