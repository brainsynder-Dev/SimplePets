package simplepets.brainsynder.api.plugin;

import lib.brainsynder.files.YamlFile;
import lib.brainsynder.utils.AdvString;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.inventory.handler.GUIHandler;
import simplepets.brainsynder.api.inventory.handler.ItemHandler;
import simplepets.brainsynder.api.other.ParticleHandler;
import simplepets.brainsynder.api.pet.PetConfigManager;
import simplepets.brainsynder.api.plugin.utils.IPetUtilities;
import simplepets.brainsynder.api.user.UserManagement;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.debug.DebugLogger;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * @throws {@link SecurityException} Only if the API is shaded into another plugin (It should not be shaded)
     * @throws {@link IllegalStateException} If the plugin is disabled.
     * @return {@link IPetsPlugin}
     */
    public static IPetsPlugin getPlugin() {
        // Uhhh ohhh... someone shaded it in
        if ((PLUGIN == null) // If this is true then it means an error occurred or it is shaded
                || (!SimplePets.class.getPackage().getName().startsWith("simplepets.brainsynder.api.plugin"))) { // Someone relocated the api in their shaded jar
            PluginDescriptionFile pdf = getCause();
            String baseMessage = "The SimplePets API was shaded into another plugin when it shouldn't be ";

            // Pardon the mess... I threw this together and didn't feel like cleaning it up
            // since it will only be used for when another plugin shades the API
            if (pdf != null) {
                baseMessage += "(plugin: "+pdf.getName();

                StringBuilder builder = new StringBuilder();
                pdf.getAuthors().forEach(author -> builder.append(author).append(", "));

                if (builder.length() != 0) {
                    baseMessage += " - by: " + AdvString.replaceLast(", ", "", builder.toString()) + ")";
                }else{
                    baseMessage += ")";
                }
            } else {
                baseMessage += "(Unable to pinpoint what plugin)";
            }

            throw new SecurityException (baseMessage);
        }
        // Check if the plugin has lost the will to live
        if (!(PLUGIN.hasFullyStarted() || PLUGIN.isStarting())) {
            throw new IllegalStateException("The plugin (SimplePets) has not enabled successfully, please check your server logs whilst starting for more information.");
        }
        return PLUGIN;
    }

    /**
     * Will try and pin-point what plugin is using the API
     *
     * @return {@link PluginDescriptionFile} or NULL if it fails
     */
    private static PluginDescriptionFile getCause () {
        Map<String, PluginDescriptionFile> map = new HashMap<>();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            PluginDescriptionFile pdf = plugin.getDescription();
            map.put(pdf.getMain(), pdf);
        }

        try {
            throw new Exception("Fetching location");
        } catch (Exception exception) {
            for (StackTraceElement element : exception.getStackTrace()) {
                String name = element.getClassName();
                if (name == null) continue;
                if (name.contains("Thread")) continue;
                if (name.startsWith(SimplePets.class.getPackage().getName())) continue;
                for (Map.Entry<String, PluginDescriptionFile> entry : map.entrySet()) {
                    if (name.toLowerCase().contains(AdvString.beforeLast(".", entry.getKey()).toLowerCase())) return entry.getValue();
                }
            }
        }

        return null;
    }

    public static YamlFile getConfiguration () {
        return getPlugin().getConfiguration();
    }

    public static UserManagement getUserManager () {
        return getPlugin().getUserManager();
    }

    public static IPetUtilities getPetUtilities () {
        return getPlugin().getPetUtilities();
    }

    public static ISpawnUtil getSpawnUtil () {
        return getPlugin().getSpawnUtil();
    }

    public static PetConfigManager getPetConfigManager () {
        return getPlugin().getPetConfigManager();
    }

    public static ItemHandler getItemHandler () {
        return getPlugin().getItemHandler();
    }

    public static GUIHandler getGUIHandler () {
        return getPlugin().getGUIHandler();
    }

    public static ParticleHandler getParticleHandler () {
        return getPlugin().getParticleHandler();
    }

    public static DebugLogger getDebugLogger () {
        return getPlugin().getDebugLogger();
    }

    public static boolean isPetEntity (Entity entity) {
        return getPlugin().isPetEntity(entity);
    }
}
