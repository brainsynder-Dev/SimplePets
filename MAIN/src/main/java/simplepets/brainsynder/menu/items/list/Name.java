package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.menu.PetSelectorGUI;

import java.io.File;
import java.util.HashMap;

@Namespace(namespace = "name")
public class Name extends Item {
    public Name(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.NAME_TAG).withName("&a&lName Pet");
    }

    @Override
    public boolean addItemToInv(PetUser owner, CustomInventory inventory) {
        return PetCore.getInstance().getConfiguration().getBoolean("RenamePet.Enabled");
    }

    @Override
    public void onClick(PetUser user, CustomInventory inventory) {
        if (!user.hasPets()) return;
        PetSelectorGUI gui = new PetSelectorGUI();
        gui.open(user, inventory, event -> {
            PetType petType = gui.getPageCache(user).getOrDefault(event.getPage(), new HashMap<>()).getOrDefault(event.getPosition(), PetType.UNKNOWN);
            SimplePets.getUserManager().getPetUser(event.getPlayer()).ifPresent(user1 -> {
                ((Player)user.getPlayer()).performCommand("pet rename "+petType.getName());
            });
            event.setWillClose(true);
            event.setWillDestroy(true);
        });
    }
}
