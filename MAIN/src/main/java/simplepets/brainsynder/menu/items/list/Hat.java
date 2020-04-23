package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.player.PetOwner;

import java.io.File;

public class Hat extends Item {
    public Hat(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.DIAMOND_HELMET).withName("&e&lToggle Pet as Hat");
    }

    @Override
    public boolean addItemToInv(PetOwner owner, CustomInventory inventory) {
        return PetCore.get().getConfiguration().getBoolean("Allow-Pets-Being-Hats");
    }

    @Override
    public void onClick(PetOwner owner, CustomInventory inventory) {
        if (owner.hasPet())
            owner.getPet().toggleHat();
    }
}
