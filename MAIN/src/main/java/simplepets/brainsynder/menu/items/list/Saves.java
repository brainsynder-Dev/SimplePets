package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
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
                .withName("&#e3c79a&lPet Saves")
                .addLore("&7", "&7View the pets you have saved");
    }

    @Override
    public boolean addItemToInv(PetUser user, CustomInventory inventory) {
        return ConfigOption.INSTANCE.PET_SAVES_ENABLED.getValue();
    }

    @Override
    public void onClick(PetUser user, CustomInventory inventory, IEntityPet pet) {
        InventoryManager.PET_SAVES.open(user);
    }
}