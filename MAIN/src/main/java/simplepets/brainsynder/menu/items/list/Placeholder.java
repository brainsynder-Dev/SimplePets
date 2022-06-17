package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.user.PetUser;

import java.io.File;

@Namespace(namespace = "placeholder")
public class Placeholder extends Item {
    public Placeholder(File file) {
        super(file);
    }

    @Override
    public void onClick(PetUser user, CustomInventory inventory, IEntityPet pet) {

    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).withName(" ");
    }
}
