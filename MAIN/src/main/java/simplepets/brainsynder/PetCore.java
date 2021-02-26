package simplepets.brainsynder;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.commands.CommandRegistry;
import lib.brainsynder.metric.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.inventory.handler.GUIHandler;
import simplepets.brainsynder.api.inventory.handler.ItemHandler;
import simplepets.brainsynder.api.pet.PetConfigManager;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.IPetsPlugin;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.UserManagement;
import simplepets.brainsynder.commands.PetsCommand;
import simplepets.brainsynder.files.Config;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.impl.PetConfiguration;
import simplepets.brainsynder.impl.PetOwner;
import simplepets.brainsynder.listeners.*;
import simplepets.brainsynder.managers.*;
import simplepets.brainsynder.sql.InventorySQL;
import simplepets.brainsynder.sql.PlayerSQL;
import simplepets.brainsynder.utils.Debug;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PetCore extends JavaPlugin implements IPetsPlugin {
    private static PetCore instance;

    private File itemFolder;

    private Config configuration;

    private ISpawnUtil SPAWN_UTIL; // TODO: Init
    private UserManager USER_MANAGER;
    private PetConfiguration PET_CONFIG;
    private RenameManager renameManager;
    private ItemManager itemManager;
    private InventoryManager inventoryManager;
    private ParticleManager particleManager;

    @Override
    public void onEnable() {
        Debug.init(this);

        instance = this;
        itemFolder = new File(getDataFolder().toString()+File.separator+"Items");

        MessageFile.init(getDataFolder());
        configuration = new Config(this);
        new InventorySQL();
        reloadSpawner();

        SimplePets.setPLUGIN(this);
        handleManagers();

        new PlayerSQL();

        handleMetrics();

        try {
            new CommandRegistry<>(this).register(new PetsCommand(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        handleListeners ();

        if (Bukkit.getOnlinePlayers().isEmpty()) return;
        // Delay it for a second to actually have the database load
        new BukkitRunnable() {
            @Override
            public void run() {
                UserManagement userManager = SimplePets.getUserManager();
                Bukkit.getOnlinePlayers().forEach(userManager::getPetUser);
            }
        }.runTaskLater(this, 20);
    }

    @Override
    public void onDisable() {
        USER_MANAGER.getAllUsers().forEach(user -> ((PetOwner) user).markForRespawn());

        USER_MANAGER = null;
        PET_CONFIG = null;
        SPAWN_UTIL = null;

        configuration = null;
        PlayerSQL.getInstance().disconnect();
        InventorySQL.getInstance().disconnect();
    }

    private void handleManagers () {
        particleManager = new ParticleManager(this);
        renameManager = new RenameManager(this);
        PET_CONFIG = new PetConfiguration(this);
        USER_MANAGER = new UserManager(this);

        itemManager = new ItemManager();
        itemManager.initiate();

        inventoryManager = new InventoryManager();
        inventoryManager.initiate();

    }

    // Registers all listeners
    private void handleListeners () {
        getServer().getPluginManager().registerEvents(new DataGUIListener(), this);
        getServer().getPluginManager().registerEvents(new SelectionGUIListener(), this);
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
        getServer().getPluginManager().registerEvents(new ChunkUnloadListener(), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveListeners(), this);
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
    public ItemHandler getItemHandler() {
        return itemManager;
    }

    @Override
    public GUIHandler getGUIHandler() {
        return inventoryManager;
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

    public RenameManager getRenameManager() {
        return renameManager;
    }

    public File getItemFolder() {
        return itemFolder;
    }

    public ParticleManager getParticleManager() {
        return particleManager;
    }

    public static PetCore getInstance() {
        return instance;
    }
}
