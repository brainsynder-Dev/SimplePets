package simplepets.brainsynder.menu.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.menu.items.list.*;

import java.util.ArrayList;
import java.util.List;

public class ItemLoaders {
    private static final PetCore plugin = PetCore.get();
    public static Air AIR;
    public static Hat HAT;
    public static Name NAME;
    public static NextPage NEXT;
    public static PreviousPage PREVIOUS;
    public static Remove REMOVE;
    public static Ride RIDE;
    public static Storage STORAGE;
    public static Placeholder PLACEHOLDER;
    public static Data DATA;
    private static List<CustomItem> loaders = new ArrayList<>();

    public static void initiate() {
        PetCore core = PetCore.get();
        PetCore.get().debug("Initializing Menu Items...");
        if (loaders != null) if (!loaders.isEmpty()) loaders.clear();
        PetCore.get().debug("Loading Customizable Item Files...");

        loaders.add(new Air (CustomItem.getLocation(core, Air.class)));
        loaders.add(new Hat (CustomItem.getLocation(core, Hat.class)));
        loaders.add(new Name (CustomItem.getLocation(core, Name.class)));
        loaders.add(new NextPage (CustomItem.getLocation(core, NextPage.class)));
        loaders.add(new PreviousPage (CustomItem.getLocation(core, PreviousPage.class)));
        loaders.add(new Remove (CustomItem.getLocation(core, Remove.class)));
        loaders.add(new Ride (CustomItem.getLocation(core, Ride.class)));
        loaders.add(new Storage (CustomItem.getLocation(core, Storage.class)));
        loaders.add(new Placeholder (CustomItem.getLocation(core, Placeholder.class)));
        loaders.add(new Data(CustomItem.getLocation(core, Data.class)));
        for (CustomItem loader : loaders) {
            loader.save();
        }
        PetCore.get().debug("Files have been loaded.");


        AIR = getLoader(Air.class);
        HAT = getLoader(Hat.class);
        NAME = getLoader(Name.class);
        NEXT = getLoader(NextPage.class);
        PREVIOUS = getLoader(PreviousPage.class);
        REMOVE = getLoader(Remove.class);
        RIDE = getLoader(Ride.class);
        STORAGE = getLoader(Storage.class);
        PLACEHOLDER = getLoader(Placeholder.class);
        DATA = getLoader(Data.class);
    }

    public static void reloadLoaders() {
        loaders.clear();
        initiate();
    }

    public static <T extends CustomItem> T getLoader(Class<T> clazz) {
        if (loaders == null) initiate();
        if (loaders.isEmpty()) initiate();
        for (CustomItem loader : loaders) {
            if (clazz.isAssignableFrom(loader.getClass())) {
                return (T) loader;
            }
        }
        return null;
    }

    public static CustomItem getLoader(String namespace) {
        if (loaders == null) initiate();
        if (loaders.isEmpty()) initiate();
        if (namespace.equalsIgnoreCase("air")) return AIR;
        for (CustomItem loader : loaders) {
            if (loader.namespace().equalsIgnoreCase(namespace)) {
                return loader;
            }
        }
        return null;
    }

    public static CustomItem getLoader(ItemStack item) {
        if (loaders == null) initiate();
        if (loaders.isEmpty()) initiate();
        if (item.getType() == Material.AIR) return AIR;
        for (CustomItem loader : loaders) {
            if (PetCore.get().getUtilities().isSimilar(loader.getItem(), item)) {
                return loader;
            }
        }
        return null;
    }
}
