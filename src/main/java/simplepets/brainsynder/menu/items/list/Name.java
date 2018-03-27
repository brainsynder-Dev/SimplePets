package simplepets.brainsynder.menu.items.list;

import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.utils.ItemBuilder;

import java.io.File;

public class Name extends Item {
    public Name(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.NAME_TAG).withName("&a&lName Pet");
    }

    @Override
    public boolean addItemToInv(PetOwner owner, CustomInventory inventory) {
        return PetCore.get().getConfiguration().getBoolean("RenamePet.Enabled");
    }

    @Override
    public void onClick(PetOwner owner, CustomInventory inventory) {
        owner.renamePet();
    }
}
