package simplepets.brainsynder.api.plugin;

import lib.brainsynder.files.YamlFile;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.entity.misc.IEntityBase;
import simplepets.brainsynder.api.inventory.handler.GUIHandler;
import simplepets.brainsynder.api.inventory.handler.ItemHandler;
import simplepets.brainsynder.api.other.ParticleHandler;
import simplepets.brainsynder.api.pet.PetConfigManager;
import simplepets.brainsynder.api.plugin.utils.IPetUtilities;
import simplepets.brainsynder.api.user.UserManagement;
import simplepets.brainsynder.debug.DebugLogger;

import java.util.Optional;

/**
 * This is the main interface for the plugin, it is used to get the main classes of the plugin.
 */
public interface IPetsPlugin extends Plugin {
    /**
     * Get the configuration file for the plugin.
     *
     * @return The configuration file.
     */
    YamlFile getConfiguration();

    IPetUtilities getPetUtilities();

    /**
     * Get the user manager.
     *
     * @return The UserManagement object.
     */
    UserManagement getUserManager();

    /**
     * Returns a spawn util object that can be used to spawn entities
     *
     * @return The SpawnUtil object.
     */
    ISpawnUtil getSpawnUtil();

    /**
     * Fetches the per-pet-type config file for the selected pet type
     *
     * @return The PetConfigManager object.
     */
    PetConfigManager getPetConfigManager();

    /**
     * Returns the item handler object, which allows you to register your own custom items
     * as well as fetch already registered items
     *
     * @return The item handler.
     */
    ItemHandler getItemHandler();

    /**
     * Returns the GUIHandler object, which allows you to register your own custom GUIs
     *
     * @return The GUIHandler object.
     */
    GUIHandler getGUIHandler();

    /**
     * Returns the particle handler, which allows you to spawn particles that the server owner customized
     *
     * @return The ParticleHandler object.
     */
    ParticleHandler getParticleHandler();

    /**
     * Returns the debug logger which allows you to send messages
     * to either console or the OP staff, can be used for info, warnings, errors, & updates
     *
     * @return A reference to the DebugLogger object.
     */
    DebugLogger getDebugLogger();

    /**
     * Returns true if the server has fully started
     *
     * @return A boolean value.
     */
    boolean hasFullyStarted();

    /**
     * Returns true if the server is starting
     *
     * @return A boolean value.
     */
    boolean isStarting();

    /**
     * If the entity is a pet, return true, otherwise return false.
     *
     * @param entity The entity to check
     * @return A boolean value.
     */
    default boolean isPetEntity(Entity entity) {
        Optional<Object> optional = getSpawnUtil().getHandle(entity);
        return optional.filter(o -> (o instanceof IEntityBase)).isPresent();
    }
}
