package simplepets.brainsynder.menu.items.list;

import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.utils.ItemBuilder;

import java.io.File;

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
    public void onClick(PetOwner owner, CustomInventory inventory) {
        PetCore.get().getInvLoaders().SAVES.open(owner);
    }
}
