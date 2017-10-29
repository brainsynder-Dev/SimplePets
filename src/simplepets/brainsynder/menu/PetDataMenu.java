package simplepets.brainsynder.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import simple.brainsynder.storage.IStorage;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.holders.PetDataHolder;
import simplepets.brainsynder.pet.IPet;

import java.util.Arrays;
import java.util.List;

import static simplepets.brainsynder.utils.LoaderRetriever.*;

public class PetDataMenu {
    private Inventory inv;
    private List<Integer> clearSlots = Arrays.asList(19, 20, 21, 22, 23, 24, 25);

    public PetDataMenu(IPet pet) {
        inv = Bukkit.createInventory(new PetDataHolder(), 54, PetCore.get().getMessages().getString("Data-Menu.PetCore-Name", true));
        setItems(pet);
    }

    public void showTo(Player p) {
        if (!PetCore.get().getConfiguration().getBoolean("PetDataMenu-Enabled"))
            return;
        if (PetCore.get().getConfiguration().getBoolean("PetItemStorage.Enable"))
            inv.setItem(storageLoader.getSlot(), storageLoader.getItem());
        if (PetCore.get().getConfiguration().getBoolean("Allow-Pets-Being-Hats"))
            inv.setItem(hatLoader.getSlot(), hatLoader.getItem());
        if (PetCore.get().getConfiguration().getBoolean("Allow-Pets-Being-Mounts"))
            inv.setItem(rideLoader.getSlot(), rideLoader.getItem());
        inv.setItem(removeLoader.getSlot(), removeLoader.getItem());
        if (PetCore.get().getConfiguration().getBoolean("PlayerPetNaming"))
            inv.setItem(namePetLoader.getSlot(), namePetLoader.getItem());
        p.openInventory(inv);
    }

    public void setItems(IPet pet) {
        if (pet == null) {
            return;
        }
        int placeHolder = inv.getSize();
        while (placeHolder > 0) {
            int i = (placeHolder - 1);
            if (!clearSlots.contains(i))
                inv.setItem(i, placeholderLoader.getItem());
            placeHolder--;
        }
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
}
