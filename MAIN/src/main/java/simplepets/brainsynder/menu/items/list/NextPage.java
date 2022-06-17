package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.utils.ListPager;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.inventory.PetTypeStorage;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.menu.inventory.SavesMenu;
import simplepets.brainsynder.menu.inventory.SelectionMenu;

import java.io.File;

@Namespace(namespace = "nextpage")
public class NextPage extends Item {
    public NextPage(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#e3c79a&l----&#e3aa4f&l>")
                .addLore("&#d1c9c9Click Here to go", "&#d1c9c9the next page")
                .setTexture("http://textures.minecraft.net/texture/956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311");
    }

    @Override
    public boolean addItemToInv(PetUser owner, CustomInventory inventory) {
        if (inventory instanceof SelectionMenu) {
            return ((SelectionMenu) inventory).getPages(owner).totalPages() > inventory.getCurrentPage(owner);
        }

        if (inventory instanceof SavesMenu) {
            return ((SavesMenu) inventory).getPages(owner).totalPages() > inventory.getCurrentPage(owner);
        }

        return false;
    }

    @Override
    public void onClick(PetUser owner, CustomInventory inventory, IEntityPet pet) {
        if (inventory instanceof SelectionMenu) {
            SelectionMenu menu = (SelectionMenu)inventory;
            ListPager<PetTypeStorage> pages = menu.getPages(owner);
            int current = menu.getCurrentPage(owner);

            if (pages == null) return;

            if (pages.totalPages() > current) menu.open(owner, (current+1));
            return;
        }

        inventory.open(owner, inventory.getCurrentPage(owner)+1);
    }
}
