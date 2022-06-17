package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.menu.inventory.SavesMenu;
import simplepets.brainsynder.menu.inventory.SelectionMenu;

import java.io.File;

@Namespace(namespace = "previouspage")
public class PreviousPage extends Item {
    public PreviousPage(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#e3aa4f&l<&#e3c79a&l----")
                .addLore("&#d1c9c9Click Here to go", "&#d1c9c9the previous page")
                .setTexture("http://textures.minecraft.net/texture/ee79665a21e660648990505a9667589325da403813bc9d8dbd180afef4d989f0");
    }

    @Override
    public boolean addItemToInv(PetUser owner, CustomInventory inventory) {
        if (inventory instanceof SelectionMenu || inventory instanceof SavesMenu) {
            return inventory.getCurrentPage(owner) > 1;
        }

        return false;
    }

    @Override
    public void onClick(PetUser owner, CustomInventory inventory, IEntityPet pet) {

        int current = inventory.getCurrentPage(owner);
        if (current > 1) {
            inventory.open(owner, (current - 1));
        }
    }
}
