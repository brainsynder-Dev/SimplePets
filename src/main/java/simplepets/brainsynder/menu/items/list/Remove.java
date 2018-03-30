package simplepets.brainsynder.menu.items.list;

import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.inventory.list.DataMenu;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.utils.ItemBuilder;

import java.io.File;

public class Remove extends Item {
    public Remove(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.BARRIER).withName("&4Remove Pet");
    }

    @Override
    public void onClick(PetOwner owner, CustomInventory inventory) {
        if (owner.hasPet()) {
            owner.removePet();
            if (inventory instanceof DataMenu) {
                PetCore.get().getInvLoaders().PET_DATA.open(owner);
            }
        }
    }
}
