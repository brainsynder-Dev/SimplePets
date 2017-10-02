package simplepets.brainsynder.utils;

import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.loaders.ItemLoader;
import simplepets.brainsynder.loaders.list.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoaderRetriever {
    private static final PetCore plugin = PetCore.get();
    public static HatLoader hatLoader;
    public static NamePetLoader namePetLoader;
    public static NextPageLoader nextPageLoader;
    public static PreviousPageLoader previousPageLoader;
    public static RemoveLoader removeLoader;
    public static RideLoader rideLoader;
    public static StorageLoader storageLoader;
    public static PlaceholderLoader placeholderLoader;
    private static List<ItemLoader> loaders = new ArrayList<>();

    public static void initiate() {
        PetCore.get().debug("Loading MenuItems...");
        if (loaders != null) if (!loaders.isEmpty()) loaders.clear();
        PetCore.get().debug("Loading Customizable Items");
        File folder = new File(plugin.getDataFolder().toString() + "/MenuItems/");
        loaders.add(new HatLoader(new File(folder, "HatItem.json")));
        loaders.add(new NamePetLoader(new File(folder, "NameItem.json")));
        loaders.add(new NextPageLoader(new File(folder, "NextPageItem.json")));
        loaders.add(new PreviousPageLoader(new File(folder, "PreviousPageItem.json")));
        loaders.add(new RemoveLoader(new File(folder, "RemoveItem.json")));
        loaders.add(new RideLoader(new File(folder, "MountItem.json")));
        loaders.add(new StorageLoader(new File(folder, "StorageItem.json")));
        loaders.add(new PlaceholderLoader(new File(folder, "PlaceholderItem.json")));
        for (ItemLoader loader : loaders) loader.save();
        hatLoader = getLoader(HatLoader.class);
        namePetLoader = getLoader(NamePetLoader.class);
        nextPageLoader = getLoader(NextPageLoader.class);
        previousPageLoader = getLoader(PreviousPageLoader.class);
        removeLoader = getLoader(RemoveLoader.class);
        rideLoader = getLoader(RideLoader.class);
        storageLoader = getLoader(StorageLoader.class);
        placeholderLoader = getLoader(PlaceholderLoader.class);
    }

    public static void reloadLoaders() {
        loaders.clear();
        initiate();
    }

    public static <T extends ItemLoader> T getLoader(Class<T> clazz) {
        if (loaders == null) initiate();
        if (loaders.isEmpty()) initiate();
        for (ItemLoader loader : loaders) {
            if (clazz.isAssignableFrom(loader.getClass())) {
                return (T) loader;
            }
        }
        return null;
    }
}
