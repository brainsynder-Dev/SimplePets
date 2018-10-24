package simplepets.brainsynder.menu.inventory;

import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.menu.inventory.list.DataMenu;
import simplepets.brainsynder.menu.inventory.list.SavesMenu;
import simplepets.brainsynder.menu.inventory.list.SelectionMenu;

import java.util.ArrayList;
import java.util.List;

public class InvLoaders {
    public SelectionMenu SELECTION;
    public DataMenu PET_DATA;
    public SavesMenu SAVES;
    private List<CustomInventory> loaders = new ArrayList<>();

    public void initiate() {
        PetCore core = PetCore.get();
        PetCore.get().debug("Initializing Inventories...");
        if (loaders != null) if (!loaders.isEmpty()) loaders.clear();
        PetCore.get().debug("Loading Customizable Inventories Files...");

        loaders.add(new SelectionMenu (CustomInventory.getLocation(core, SelectionMenu.class)));
        loaders.add(new DataMenu (CustomInventory.getLocation(core, DataMenu.class)));
        loaders.add(new SavesMenu (CustomInventory.getLocation(core, SavesMenu.class)));

        for (CustomInventory loader : loaders) {
            loader.save();
            loader.load();
        }
        PetCore.get().debug("Files have been loaded.");


        SELECTION = getLoader(SelectionMenu.class);
        PET_DATA = getLoader(DataMenu.class);
        SAVES = getLoader(SavesMenu.class);
    }

    public void reloadLoaders() {
        loaders.clear();
        initiate();
    }

    public <T extends CustomInventory> T getLoader(Class<T> clazz) {
        if (loaders == null) initiate();
        if (loaders.isEmpty()) initiate();
        for (CustomInventory loader : loaders) {
            if (clazz.isAssignableFrom(loader.getClass())) {
                return (T) loader;
            }
        }
        return null;
    }
}
