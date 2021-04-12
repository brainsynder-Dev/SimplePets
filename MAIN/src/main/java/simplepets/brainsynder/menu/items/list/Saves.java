package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.managers.InventoryManager;

import java.io.File;

@Namespace(namespace = "saves")
public class Saves extends Item {
    public Saves(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.COMMAND_BLOCK)
                .withName("&e&lPet Saves")
                .addLore("&7", "&7View the pets you have saved");
    }

    @Override
    public void onClick(PetUser user, CustomInventory inventory) {
        InventoryManager.PET_SAVES.open(user);
    }
}