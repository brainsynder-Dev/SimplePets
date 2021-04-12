package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.menu.PetSelectorGUI;

import java.io.File;
import java.util.HashMap;

@Namespace(namespace = "remove")
public class Remove extends Item {

    public Remove(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#b35349&lRemove Pets").addLore("&#d1c9c9Click Here to remove all pets", "&#d1c9c9Shift+Click to select a pet")
                .setTexture("http://textures.minecraft.net/texture/beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7");
    }

    @Override
    public void onClick(PetUser user, CustomInventory inventory) {
        if (!user.hasPets()) return;
        user.removePets();
        user.updateDataMenu();
    }

    @Override
    public void onShiftClick(PetUser user, CustomInventory inventory) {
        PetSelectorGUI gui = new PetSelectorGUI();
        gui.open(user, inventory, event -> {
            PetType petType = gui.getPageCache(user).getOrDefault(event.getPage(), new HashMap<>()).getOrDefault(event.getPosition(), PetType.UNKNOWN);
            SimplePets.getUserManager().getPetUser(event.getPlayer()).ifPresent(user1 -> {
                user1.removePet(petType);
                user.updateDataMenu();
            });
            event.setWillClose(true);
            event.setWillDestroy(true);
        });
    }
}
