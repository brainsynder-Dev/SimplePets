package simplepets.brainsynder.menu.inventory.list;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simple.brainsynder.utils.ObjectPager;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.event.inventory.PetInventoryOpenEvent;
import simplepets.brainsynder.menu.holders.SelectionHolder;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.menu.items.list.*;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.PetMap;
import simplepets.brainsynder.storage.PetTypeStorage;

import java.io.File;
import java.util.*;

public class SelectionMenu extends CustomInventory {
    private List<PetType> availableTypes;
    private Map<String, ObjectPager<PetTypeStorage>> pagerMap;
    private PetMap<String, IStorage<PetTypeStorage>> petMap;

    public SelectionMenu(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        availableTypes = new ArrayList<>();
        pagerMap = new HashMap<>();
        petMap = new PetMap<>();

        defaults.put("size", "54");
        defaults.put("title", "&a&lSelect a Pet:");

        Map<Integer, String> object = new HashMap<> ();
        Arrays.asList(
                11, 12, 13, 14, 15, 16, 17,
                20, 21, 22, 23, 24, 25, 26,
                29, 30, 31, 32, 33, 34, 35,
                38, 39, 40, 41, 42, 43, 44
        ).forEach(slot -> object.put(slot, "air"));
        object.put(5, "name");
        object.put(9, "data");
        object.put(46, "previouspage");
        object.put(49, "ride");
        object.put(50, "remove");
        object.put(51, "hat");
        object.put(54, "nextpage");

        Set<Map.Entry<Integer, String>> set = object.entrySet();
        List<Map.Entry<Integer, String>> list = new ArrayList<>(set);
        Collections.sort(list, Comparator.comparing(o -> (o.getKey())));

        JSONArray array = new JSONArray();
        for (Map.Entry<Integer, String> entry : list) {
            JSONObject json = new JSONObject();
            json.put("slot", entry.getKey());
            json.put("item", entry.getValue());
            array.add(json);
        }

        defaults.put("slots", array);

        for (PetType type : PetType.values()) {
            if (type.isSupported() && type.isEnabled()) {
                availableTypes.add(type);
            }
        }
    }

    @Override
    public void open(PetOwner owner, int page) {
        if (!isEnabled()) return;
        pageSave.put(owner.getPlayer().getName(), page);
        Inventory inv = Bukkit.createInventory(new SelectionHolder(), getSize(), getTitle());
        int placeHolder = inv.getSize();
        int maxPets = 0;
        while (placeHolder > 0) {
            int slot = (placeHolder - 1);
            if (getSlots().containsKey(slot)) {
                Item item = getSlots().get(slot);
                if (item instanceof Air) {
                    maxPets++;
                } else {
                    inv.setItem(placeHolder - 1, PetCore.get().getItemLoaders().PLACEHOLDER.getItem());
                }
            } else {
                inv.setItem(placeHolder - 1, PetCore.get().getItemLoaders().PLACEHOLDER.getItem());
            }
            placeHolder--;
        }

        IStorage<PetTypeStorage> petTypes = new StorageList<>();
        for (PetType type : availableTypes) {
            if (type.hasPermission(owner.getPlayer())) {
                petTypes.add(new PetTypeStorage(type));
            }
        }

        ObjectPager<PetTypeStorage> pages = new ObjectPager<>(maxPets, petTypes.toArrayList());
        if (pagerMap.containsKey(owner.getPlayer().getName())) {
            pages = pagerMap.get(owner.getPlayer().getName());
        } else {
            pagerMap.put(owner.getPlayer().getName(), pages);
        }

        ObjectPager<PetTypeStorage> finalPages = pages;
        getSlots().forEach((slot, item) -> {
            if (item instanceof PreviousPage) {
                if ((getCurrentPage(owner) > 1))
                    inv.setItem(slot, item.getItem());
            } else if (item instanceof NextPage) {
                if (finalPages.totalPages() > getCurrentPage(owner))
                    inv.setItem(slot, item.getItem());
            } else if (item instanceof Hat) {
                if (PetCore.get().getConfiguration().getBoolean("Allow-Pets-Being-Hats"))
                    inv.setItem(slot, item.getItem());
            } else if (item instanceof Ride) {
                if (PetCore.get().getConfiguration().getBoolean("Allow-Pets-Being-Mounts"))
                    inv.setItem(slot, item.getItem());
            } else if (item instanceof Name) {
                if (PetCore.get().getConfiguration().getBoolean("RenamePet.Enabled"))
                    inv.setItem(slot, item.getItem());
            } else {
                inv.setItem(slot, item.getItem());
            }
        });

        PetInventoryOpenEvent event = new PetInventoryOpenEvent(pages.getPage(page), owner.getPlayer());
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        IStorage<ItemStack> types = event.getItems().copy();
        while (types.hasNext()) {
            inv.addItem(types.next());
        }
        petMap.put(owner.getPlayer().getName(), event.getShownPetTypes());
        owner.getPlayer().openInventory(inv);
    }

    public PetMap<String, IStorage<PetTypeStorage>> getPetMap() {
        return petMap;
    }

    public ObjectPager<PetTypeStorage> getPages(PetOwner owner) {
        if (pagerMap.containsKey(owner.getPlayer().getName()))
            return pagerMap.get(owner.getPlayer().getName());
        return null;
    }

    @Override
    public void reset(PetOwner owner) {
        super.reset(owner);

        petMap.remove(owner.getPlayer().getName());
        pagerMap.remove(owner.getPlayer().getName());
    }
}
