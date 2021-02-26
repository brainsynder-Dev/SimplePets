package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.menu.PetSelectorGUI;

import java.io.File;
import java.util.HashMap;

@Namespace(namespace = "hat")
public class Hat extends Item {
    public Hat(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.DIAMOND_HELMET).withName("&e&lToggle Pet as Hat");
    }

    @Override
    public boolean addItemToInv(PetUser user, CustomInventory inventory) {
        return PetCore.getInstance().getConfiguration().getBoolean("PetToggles.All-Pets-Hat");
    }

    @Override
    public void onClick(PetUser user, CustomInventory inventory) {
        if (!user.hasPets()) return;
        PetSelectorGUI gui = new PetSelectorGUI();
        gui.open(user, inventory, event -> {
            PetType petType = gui.getPageCache(user).getOrDefault(event.getPage(), new HashMap<>()).getOrDefault(event.getPosition(), PetType.UNKNOWN);
            user.setPetHat(petType, !user.isPetHat(petType));
            event.setWillClose(true);
            event.setWillDestroy(true);
        });
    }
}
