package simplepets.brainsynder.managers;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.utils.TaskTimer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.inventory.handler.ItemHandler;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.menu.items.CustomItem;
import simplepets.brainsynder.menu.items.list.*;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.*;

public class ItemManager implements ItemHandler {
    private File registeredFolder;
    private static Air AIR;
    public static Placeholder PLACEHOLDER;
//    public RedPlaceholder RED_PLACEHOLDER;
//    public Data DATA;
    private PetCore plugin;
    private final Map<String, Item> items = new HashMap<>();

    public void initiate() {
        TaskTimer timer = new TaskTimer(getClass(), "initiate");
        timer.start();
        plugin = PetCore.getInstance();
        SimplePets.getDebugLogger().debug(DebugBuilder.build(getClass()).setMessages("Initializing Menu Items..."));
        if (items != null) if (!items.isEmpty()) items.clear();
        timer.label("clearing old data (if any)");

        SimplePets.getDebugLogger().debug(DebugBuilder.build(getClass()).setMessages("Loading Customizable Item Files..."));
        File customFolder = new File(plugin.getDataFolder() + "/Items/Custom/");
        registeredFolder = new File(plugin.getDataFolder() + "/Items/AddonItems/");
        if (!customFolder.exists()) customFolder.mkdirs();
        if (!registeredFolder.exists()) registeredFolder.mkdirs();
        timer.label("folder generation");

        add(new Air (getLocation(plugin, Air.class)));
        add(new Hat (getLocation(plugin, Hat.class)));
        add(new Name (getLocation(plugin, Name.class)));
        add(new NextPage (getLocation(plugin, NextPage.class)));
        add(new PreviousPage(getLocation(plugin, PreviousPage.class)));
        add(new Remove (getLocation(plugin, Remove.class)));
        add(new Installer (getLocation(plugin, Installer.class)));
        add(new Ride (getLocation(plugin, Ride.class)));
//        add(new Storage (getLocation(plugin, Storage.class)));
        add(new Placeholder (getLocation(plugin, Placeholder.class)));
//        add(new Data(getLocation(plugin, Data.class)));
        add(new SavePet(getLocation(plugin, SavePet.class)));
        add(new Saves(getLocation(plugin, Saves.class)));
//        add(new RedPlaceholder(getLocation(plugin, RedPlaceholder.class)));
//        add(new Update(getLocation(plugin, Update.class)));
//        add(new FlameOn(new File(customFolder, "flameon.json")));
        timer.label("items registered");

        for (Item loader : items.values()) {
            (loader).save();
        }
        timer.label("saving files");
        SimplePets.getDebugLogger().debug(DebugBuilder.build(getClass()).setMessages("Files have been loaded."));


        // Loads the custom items that the were added
        List<File> files = Arrays.asList(customFolder.listFiles());
        if (!files.isEmpty()) {
            for (File file : files) {
                if (!file.getName().endsWith(".json")) continue;

                Item item = new CustomItem(file) {
                    @Override
                    public ItemBuilder getDefaultItem() {
                        return new ItemBuilder(Material.STONE);
                    }
                };
                if (item.hasKey("namespace") && item.isEnabled()) {
                    items.putIfAbsent(item.getString("namespace"), item);
                }
            }
        }
        timer.label("fetched custom items");

        AIR = getItem(Air.class).get();
        PLACEHOLDER = getItem(Placeholder.class).get();
//        RED_PLACEHOLDER = getLoader(RedPlaceholder.class);
//        DATA = getLoader(Data.class);
        timer.stop();
    }


    public static File getLocation (PetCore core, Class<? extends Item> clazz) {
        File folder = new File(core.getDataFolder() + "/Items/");
        return new File(folder, clazz.getSimpleName() + ".json");
    }

    private void add (Item item) {
        item.getItemData().ifPresent(itemData -> items.putIfAbsent(itemData.namespace(), item));
    }

    public void reloadLoaders() {
        items.clear();
        initiate();
    }

    @Override
    public boolean register(Class<? extends Item> clazz) {
        Item item;

        if (!clazz.isAnnotationPresent(Namespace.class)) throw new MissingResourceException("Missing ItemData annotation", clazz.getName(), "@ItemData");

        Namespace data = clazz.getAnnotation(Namespace.class);
        String name = data.namespace();

        try {
            Constructor constructor = clazz.getConstructor(File.class);
            item = (Item) constructor.newInstance(new File(registeredFolder, name+".json"));
        }catch (Exception e) {
            return false;
        }

        if (items.containsKey(name)) return false;
        item.save();
        items.put(name, item);
        return true;
    }

    @Override
    public <T extends Item> Optional<T> getItem(Class<T> clazz) {
        if (items == null) initiate();
        if (items.isEmpty()) initiate();
        for (Item loader : items.values()) {
            if (clazz.isAssignableFrom(loader.getClass())) {
                return Optional.of((T) loader);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Item> getItem(String namespace) {
        if (items == null) initiate();
        if (items.isEmpty()) initiate();
        if (namespace.equalsIgnoreCase("air")) return Optional.of(AIR);
        if (items.containsKey(namespace)) {
            return Optional.of(items.get(namespace));
        }
        return Optional.empty();
    }


    @Override
    public Optional<Item> getItem(ItemStack item) {
        if (items == null) initiate();
        if (items.isEmpty()) initiate();
        if (item.getType() == Material.AIR) return Optional.of(AIR);
        for (Item loader : items.values()) {
            if (loader.getItemBuilder().isSimilar(item)) {
                return Optional.of(loader);
            }
        }
        return Optional.empty();
    }
}
