package simplepets.brainsynder.menu.inventory;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.storage.IStorage;
import lib.brainsynder.storage.StorageList;
import lib.brainsynder.utils.Colorize;
import lib.brainsynder.utils.ListPager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.inventory.PetTypeStorage;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.inventory.handler.InventoryType;
import simplepets.brainsynder.api.pet.IPetConfig;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.managers.ItemManager;
import simplepets.brainsynder.menu.inventory.holders.SelectorHolder;
import simplepets.brainsynder.menu.items.list.Air;
import simplepets.brainsynder.utils.BiTask;
import simplepets.brainsynder.utils.Keys;

import java.io.File;
import java.util.*;

public class PetSelectorMenu extends CustomInventory {
    private Map<String, ListPager<PetTypeStorage>> pagerMap;
    private Map<String, BiTask<PetUser, PetType>> taskMap;

    public PetSelectorMenu(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        pagerMap = new HashMap<>();
        taskMap = new HashMap<>();

        setDefault("__COMMENT__", "This is used when you are selecting a pet EG: pet renaming via the GUI item");
        setDefault("size", 54);
        setDefault("title_comment", "It is automatically fetched from the previous GUI");

        Map<Integer, String> object = new HashMap<>();
        Arrays.asList(
                11, 12, 13, 14, 15, 16, 17,
                20, 21, 22, 23, 24, 25, 26,
                29, 30, 31, 32, 33, 34, 35,
                38, 39, 40, 41, 42, 43, 44
        ).forEach(slot -> object.put(slot, "air"));
        object.put(46, "previouspage");
        object.put(54, "nextpage");

        // Makes sure the slot numbers are sorted from low to high
        Set<Map.Entry<Integer, String>> set = object.entrySet();
        List<Map.Entry<Integer, String>> list = new ArrayList<>(set);
        list.sort(Map.Entry.comparingByKey());

        JsonArray array = new JsonArray();
        for (Map.Entry<Integer, String> entry : list) {
            JsonObject json = new JsonObject();
            json.add("slot", entry.getKey());
            json.add("item", entry.getValue());
            array.add(json);
        }

        setDefault("slots", array);
    }

    @Override
    public void onClick(int slot, ItemStack item, Player player) {

    }

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.TYPE_SELECTION_GUI;
    }

    public void setTask (String name, BiTask<PetUser, PetType> task) {
        taskMap.put(name, task);
    }

    public BiTask<PetUser, PetType> getTask (String name) {
        return taskMap.getOrDefault(name, null);
    }

    public void open (PetUser user, int page, String title) {
        Player player = user.getPlayer();
        pageSave.put(player.getName(), page);
        Inventory inv = Bukkit.createInventory(new SelectorHolder(), getInteger("size", 54), Colorize.translateBungeeHex(title));
        int placeHolder = inv.getSize();
        int maxPets = 0;
        while (placeHolder > 0) {
            int slot = (placeHolder - 1);
            if (getSlots().containsKey(slot)) {
                Item item = getSlots().get(slot);
                if (item instanceof Air) {
                    maxPets++;
                } else {
                    inv.setItem(placeHolder - 1, ItemManager.PLACEHOLDER.getItemBuilder().build());
                }
            } else {
                inv.setItem(placeHolder - 1, ItemManager.PLACEHOLDER.getItemBuilder().build());
            }
            placeHolder--;
        }

        IStorage<PetTypeStorage> petTypes = new StorageList<>();
        for (IEntityPet entity : user.getPetEntities()) {
            IPetConfig petConfig = SimplePets.getPetConfigManager().getPetConfig(entity.getPetType()).orElse(null);
            ItemBuilder builder = entity.getPetType().getBuilder();
            if (petConfig != null) builder = petConfig.getBuilder();
            petTypes.add(new PetTypeStorage(entity.getPetType()).setItem(builder.clearLore().handleMeta(ItemMeta.class, itemMeta -> {
                itemMeta.getPersistentDataContainer().set(Keys.GUI_ITEM, PersistentDataType.INTEGER, 1);
                itemMeta.getPersistentDataContainer().set(Keys.PET_TYPE_ITEM, PersistentDataType.STRING, entity.getPetType().getName());
                return itemMeta;
            }).build()));
        }

        if (petTypes.getSize() == 0) return;

        ListPager<PetTypeStorage> pages = new ListPager<>(maxPets, petTypes.toArrayList());
        pagerMap.put(player.getName(), pages);

        getSlots().forEach((slot, item) -> {
            if (item.isEnabled() && item.addItemToInv(user, this))
                inv.setItem(slot, item.getItemBuilder().build());
        });

        if (!pages.isEmpty()) {
            if (pages.exists(page)) {
                pages.getPage(page).forEach(petTypeStorage -> {
                    inv.addItem(petTypeStorage.getItem());
                });
            } else {
                SimplePets.getDebugLogger().debug(DebugLevel.WARNING, "Page does not exist (Page " + page + " / " + pages.totalPages() + ")");
            }

        }

        if (ConfigOption.INSTANCE.MISC_TOGGLES_CLEAR_ALL_PLACEHOLDERS.getValue())
            inv.remove(ItemManager.PLACEHOLDER.getItemBuilder().build());

        player.openInventory(inv);
    }


    @Override
    public void open(PetUser user, int page) {
        open(user, page, "&#de9790[] &#b35349Pets");
    }

    public ListPager<PetTypeStorage> getPages(PetUser user) {
        OfflinePlayer player = user.getPlayer();
        if (pagerMap.containsKey(player.getName()))
            return pagerMap.get(player.getName());
        return null;
    }

    @Override
    public void reset(PetUser user) {
        super.reset(user);

        String name = user.getOwnerName();
        taskMap.remove(name);
        pagerMap.remove(name);
    }
}
