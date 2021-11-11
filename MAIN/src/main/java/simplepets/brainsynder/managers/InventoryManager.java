package simplepets.brainsynder.managers;

import lib.brainsynder.utils.TaskTimer;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.handler.GUIHandler;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.menu.inventory.*;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventoryManager implements GUIHandler {
    private File registeredFolder;

    public static SelectionMenu SELECTION;
    public static DataMenu PET_DATA;
    public static SavesMenu PET_SAVES;
    public static AddonMenu ADDONS;
    public static PetSelectorMenu SELECTOR;

    private final List<CustomInventory> loaders = new ArrayList<>();

    public void initiate () {
        TaskTimer timer = new TaskTimer(getClass(), "initiate");
        timer.start();
        PetCore plugin = PetCore.getInstance();
        registeredFolder = new File(plugin.getDataFolder() + "/Inventories/AddonInventories/");
        if (!registeredFolder.exists()) registeredFolder.mkdirs();

        SimplePets.getDebugLogger().debug("Initializing Inventories...");
        if (loaders != null) if (!loaders.isEmpty()) loaders.clear();
        timer.label("clearing old data (if any)");

        SimplePets.getDebugLogger().debug("Loading Customizable Inventories Files...");
        loaders.add(new SelectionMenu(getLocation(plugin, SelectionMenu.class)));
        loaders.add(new DataMenu (getLocation(plugin, DataMenu.class)));
        loaders.add(new SavesMenu (getLocation(plugin, SavesMenu.class)));
        loaders.add(new AddonMenu(getLocation(plugin, AddonMenu.class)));
        loaders.add(new PetSelectorMenu(getLocation(plugin, PetSelectorMenu.class)));
        timer.label("inventories registered");
//        loaders.add(new ArmorMenu(CustomInventory.getLocation(core, ArmorMenu.class)));

        for (CustomInventory loader : loaders) loader.save();
        timer.label("saving files");
        SimplePets.getDebugLogger().debug("Files have been loaded.");


        SELECTION = getInventory(SelectionMenu.class).get();
        PET_DATA = getInventory(DataMenu.class).get();
        PET_SAVES = getInventory(SavesMenu.class).get();
        ADDONS = getInventory(AddonMenu.class).get();
        SELECTOR = getInventory(PetSelectorMenu.class).get();
        timer.stop();
//        ARMOR = getLoader(ArmorMenu.class);
    }


    public static File getLocation(PetCore core, Class<? extends CustomInventory> clazz) {
        File folder = new File(core.getDataFolder() + "/Inventories/");
        return new File(folder, clazz.getSimpleName() + ".json");
    }


    @Override
    public void register(Class<? extends CustomInventory> clazz) {
        CustomInventory inventory;

        try {
            Constructor constructor = clazz.getConstructor(File.class);
            inventory = (CustomInventory) constructor.newInstance(new File(registeredFolder, clazz.getSimpleName()+".json"));
        }catch (Exception e) {
            return;
        }

        inventory.save();
        loaders.add(inventory);
    }

    @Override
    public <T extends CustomInventory> Optional<T> getInventory(Class<T> clazz) {
        if (loaders == null) initiate();
        if (loaders.isEmpty()) initiate();
        for (CustomInventory loader : loaders) {
            if (clazz.isAssignableFrom(loader.getClass())) {
                return Optional.of((T) loader);
            }
        }
        return Optional.empty();
    }
}
