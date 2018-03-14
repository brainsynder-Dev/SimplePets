package simplepets.brainsynder.menu.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.menu.items.list.*;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemLoaders {
    private Air AIR;
    public Placeholder PLACEHOLDER;
    public Data DATA;
    private Map<String, Item> items = new HashMap<>();

    public void initiate() {
        PetCore core = PetCore.get();
        PetCore.get().debug("Initializing Menu Items...");
        if (items != null) if (!items.isEmpty()) items.clear();
        PetCore.get().debug("Loading Customizable Item Files...");
        File customFolder = new File(core.getDataFolder().toString() + "/Items/Custom/");
        if (!customFolder.exists()) customFolder.mkdirs();
        List<File> files = Arrays.asList(customFolder.listFiles());

        add(new Air (Item.getLocation(core, Air.class)));
        add(new Hat (Item.getLocation(core, Hat.class)));
        add(new Name (Item.getLocation(core, Name.class)));
        add(new NextPage (Item.getLocation(core, NextPage.class)));
        add(new PreviousPage (Item.getLocation(core, PreviousPage.class)));
        add(new Remove (Item.getLocation(core, Remove.class)));
        add(new Ride (Item.getLocation(core, Ride.class)));
        add(new Storage (Item.getLocation(core, Storage.class)));
        add(new Placeholder (Item.getLocation(core, Placeholder.class)));
        add(new Data(Item.getLocation(core, Data.class)));
        add(new FlameOn(new File(customFolder, "flameon.json")));

        for (Item loader : items.values()) {
            loader.save();
        }
        PetCore.get().debug("Files have been loaded.");


        if (!files.isEmpty()) {
            for (File file : files) {
                if (!file.getName().endsWith(".json")) continue;

                Item item = new Item(file);
                if (item.hasKey("namespace") && item.isEnabled()) {
                    items.putIfAbsent(item.namespace(), item);
                }
            }
        }

        AIR = getLoader(Air.class);
        PLACEHOLDER = getLoader(Placeholder.class);
        DATA = getLoader(Data.class);
    }

    private void add (Item item) {
        items.putIfAbsent(item.namespace(), item);
    }

    public void reloadLoaders() {
        items.clear();
        initiate();
    }

    private <T extends Item> T getLoader(Class<T> clazz) {
        if (items == null) initiate();
        if (items.isEmpty()) initiate();
        for (Item loader : items.values()) {
            if (clazz.isAssignableFrom(loader.getClass())) {
                return (T) loader;
            }
        }
        return null;
    }

    public Item getLoader(String namespace) {
        if (items == null) initiate();
        if (items.isEmpty()) initiate();
        if (namespace.equalsIgnoreCase("air")) return AIR;
        if (items.containsKey(namespace)) {
            return items.get(namespace);
        }
        return null;
    }

    public Item getLoader(ItemStack item) {
        if (items == null) initiate();
        if (items.isEmpty()) initiate();
        if (item.getType() == Material.AIR) return AIR;
        for (Item loader : items.values()) {
            if (PetCore.get().getUtilities().isSimilar(loader.getItem(), item)) {
                return loader;
            }
        }
        return null;
    }
}
