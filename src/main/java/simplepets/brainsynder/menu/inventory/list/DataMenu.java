package simplepets.brainsynder.menu.inventory.list;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simple.brainsynder.storage.IStorage;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.menu.holders.PetDataHolder;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.items.list.Hat;
import simplepets.brainsynder.menu.items.list.Name;
import simplepets.brainsynder.menu.items.list.PreviousPage;
import simplepets.brainsynder.menu.items.list.Ride;
import simplepets.brainsynder.menu.menuItems.base.MenuItem;
import simplepets.brainsynder.player.PetOwner;

import java.io.File;
import java.util.*;

public class DataMenu extends CustomInventory {

    public DataMenu(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        defaults.put("enabled", "true");
        defaults.put("size", "54");
        defaults.put("title", "&a&lPet Data Changer");

        Map<Integer, String> object = new HashMap<> ();
        Arrays.asList(21, 22, 23, 24, 25, 31, 33).forEach(slot -> object.put(slot, "air"));
        object.put(1, "storage");
        object.put(5, "name");
        object.put(49, "ride");
        object.put(50, "remove");
        object.put(51, "hat");

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
    }

    @Override
    public void open(PetOwner owner) {
        if (!isEnabled()) return;
        Inventory inv = Bukkit.createInventory(new PetDataHolder(), getSize(), getTitle());
        int placeHolder = inv.getSize();
        while (placeHolder > 0) {
            inv.setItem(placeHolder - 1, PetCore.get().getItemLoaders().PLACEHOLDER.getItem());
            placeHolder--;
        }

        getSlots().forEach((slot, item) -> {
            if (item instanceof PreviousPage) {
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

        if (owner.hasPet()) {
            IPet pet = owner.getPet();
            IStorage<MenuItem> items = pet.getItems().copy();
            while (items.hasNext()) {
                MenuItem item = items.next();
                try {
                    ItemStack stack = item.getItem().create();
                    if (!inv.contains(stack)) {
                        if (item.hasPermission(pet.getOwner())) {
                            inv.addItem(stack);
                        }
                    }
                } catch (Exception e) {
                    PetCore.get().debug("An Internal Error occurred when loading the pet data for the " + item.getClass().getSimpleName() + ".class");
                }
            }
        }
        owner.getPlayer().openInventory(inv);
    }
}
