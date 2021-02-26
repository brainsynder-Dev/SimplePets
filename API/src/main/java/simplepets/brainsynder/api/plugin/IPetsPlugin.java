package simplepets.brainsynder.api.plugin;

import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.inventory.handler.GUIHandler;
import simplepets.brainsynder.api.inventory.handler.ItemHandler;
import simplepets.brainsynder.api.pet.PetConfigManager;
import simplepets.brainsynder.api.user.UserManagement;

public interface IPetsPlugin extends Plugin {
    UserManagement getUserManager ();

    ISpawnUtil getSpawnUtil();

    PetConfigManager getPetConfigManager ();

    ItemHandler getItemHandler ();

    GUIHandler getGUIHandler ();
}
