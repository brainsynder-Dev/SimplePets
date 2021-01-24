package simplepets.brainsynder;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.commands.CommandRegistry;
import lib.brainsynder.metric.bukkit.Metrics;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.pet.PetConfigManager;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.IPetsPlugin;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.user.UserManagement;
import simplepets.brainsynder.commands.PetsCommand;
import simplepets.brainsynder.files.Config;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.impl.PetConfiguration;
import simplepets.brainsynder.listeners.ChunkUnloadListener;
import simplepets.brainsynder.listeners.JoinLeaveListeners;
import simplepets.brainsynder.managers.UserManager;

import java.util.HashMap;
import java.util.Map;

public class PetCore extends JavaPlugin implements IPetsPlugin {
    private static PetCore instance;

    public NamespacedKey ENTITY_OWNER;

    private Config configuration;

    private ISpawnUtil SPAWN_UTIL; // TODO: Init
    private UserManager USER_MANAGER;
    private PetConfiguration PET_CONFIG;

    @Override
    public void onEnable() {
        instance = this;
        MessageFile.init(getDataFolder());
        ENTITY_OWNER = new NamespacedKey(this, "ownerUUID");
        reloadSpawner();

        SimplePets.setPLUGIN(this);
        configuration = new Config(this);

        USER_MANAGER = new UserManager(this);
        PET_CONFIG = new PetConfiguration(this);

        handleMetrics();

        try {
            new CommandRegistry<>(this).register(new PetsCommand(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        getServer().getPluginManager().registerEvents(new ChunkUnloadListener(), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveListeners(), this);
    }

    @Override
    public void onDisable() {
        USER_MANAGER.getAllUsers().forEach(PetUser::removePets);

        USER_MANAGER = null;
        PET_CONFIG = null;
        SPAWN_UTIL = null;

        configuration = null;
    }

    @Override
    public ISpawnUtil getSpawnUtil() {
        return SPAWN_UTIL;
    }

    @Override
    public PetConfigManager getPetConfigManager() {
        return PET_CONFIG;
    }

    @Override
    public UserManagement getUserManager() {
        return USER_MANAGER;
    }


    public double getJavaVersion() {
        try {
            String version = System.getProperty("java.version");
            if (version.contains(".")) {
                int pos = version.indexOf('.');
                pos = version.indexOf('.', pos + 1);
                version = version.substring(0, pos);
            }
            return Double.parseDouble(version);
        } catch (Throwable t) {
            return 0.0;
        }
    }

    public Config getConfiguration() {
        return configuration;
    }



    public boolean hasPerm(Player p, String perm) {
        return hasPerm(p, perm, false) == 1;
    }

    public int hasPerm(Player p, String perm, boolean strict) {
        if (configuration.getBoolean("Needs-Permission")) {
            if (strict) {
                for (PermissionAttachmentInfo info : p.getEffectivePermissions()) {
                    if (info.getPermission().equalsIgnoreCase(perm)) {
                        return info.getValue() ? 1 : 0;
                    }
                }
                return -1;
            } else {
                return p.hasPermission(perm) ? 1 : 0;
            }
        } else {
            return 1;
        }
    }

    private Map<String, Integer> getActivePets() {
        Map<String, Integer> users = new HashMap<>();

        SimplePets.getUserManager().getAllUsers().forEach(user -> {
            user.getPetEntities().forEach(entityPet -> {
                PetType type = entityPet.getPetType();

                String name = type.getName();
                if (!users.containsKey(name)) {
                    users.put(name, 1);
                } else {
                    users.put(name, users.get(name) + 1);
                }
            });
        });

        return users;
    }


    private void handleMetrics () {
        Metrics metrics = new Metrics(this);
        metrics.addCustomChart(new Metrics.AdvancedPie("active_pets", this::getActivePets));
    }

    private void reloadSpawner() {
        ServerVersion version = ServerVersion.getVersion();
        try {
            Class<?> clazz = Class.forName("simplepets.brainsynder.versions." + version.name() + ".SpawnerUtil");
            if (clazz == null) return;
            if (ISpawnUtil.class.isAssignableFrom(clazz)) {
                SPAWN_UTIL = (ISpawnUtil) clazz.getConstructor().newInstance();
                //TODO: debug("Successfully Linked to " + version.name() + " SpawnUtil Class");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //TODO: debug("Could not link to a SpawnUtil Class... Possible Wrong version?");
        }
    }

    public static PetCore getInstance() {
        return instance;
    }
}
