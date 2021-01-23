package api.plugin;

import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.pet.PetConfigManager;
import simplepets.brainsynder.api.user.UserManagement;

public final class SimplePets {
    private static IPetsPlugin PLUGIN;

    public static void setPLUGIN(IPetsPlugin plugin) {
        if(PLUGIN != null) return;
        PLUGIN = plugin;
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
}
