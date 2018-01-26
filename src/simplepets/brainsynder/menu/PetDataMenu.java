package simplepets.brainsynder.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import simple.brainsynder.storage.IStorage;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.menu.holders.PetDataHolder;
import simplepets.brainsynder.menu.menuItems.base.MenuItem;

import java.util.Arrays;
import java.util.List;

import static simplepets.brainsynder.menu.items.ItemLoaders.PLACEHOLDER;

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
        /*if (PetCore.get().getConfiguration().getBoolean("PetItemStorage.Enable"))
            inv.setItem(STORAGE.getSlot(), STORAGE.getItem());
        if (PetCore.get().getConfiguration().getBoolean("Allow-Pets-Being-Hats"))
            inv.setItem(HAT.getSlot(), HAT.getItem());
        if (PetCore.get().getConfiguration().getBoolean("Allow-Pets-Being-Mounts"))
            inv.setItem(RIDE.getSlot(), RIDE.getItem());
        inv.setItem(REMOVE.getSlot(), REMOVE.getItem());
        if (PetCore.get().getConfiguration().getBoolean("PlayerPetNaming"))
            inv.setItem(NAME.getSlot(), NAME.getItem());*/
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
                inv.setItem(i, PLACEHOLDER.getItem());
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
