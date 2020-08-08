package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.files.Config;

import java.io.File;

public class Ride extends Item {
    public Ride(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.DIAMOND_HORSE_ARMOR).withName("&e&lToggle Pet Riding");
    }

    @Override
    public boolean addItemToInv(PetOwner owner, CustomInventory inventory) {
        return PetCore.get().getConfiguration().getBoolean(Config.MOUNTABLE);
    }

    @Override
    public void onClick(PetOwner owner, CustomInventory inventory) {
        if (owner.hasPet())
            owner.getPet().toggleRiding(false);
    }
}
