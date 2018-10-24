package simplepets.brainsynder.menu.items.list;

import simple.brainsynder.utils.ObjectPager;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.inventory.list.SavesMenu;
import simplepets.brainsynder.menu.inventory.list.SelectionMenu;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.PetTypeStorage;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.utils.Utilities;

import java.io.File;

public class NextPage extends Item {
    public NextPage(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return Utilities.getSkullMaterial(Utilities.SkullType.PLAYER).toBuilder(1)
                .withName("&6&l&m----&6&l>")
                .addLore("&7Click Here to go","&7the next page")
                .setTexture("http://textures.minecraft.net/texture/c2f910c47da042e4aa28af6cc81cf48ac6caf37dab35f88db993accb9dfe516");
    }

    @Override
    public boolean addItemToInv(PetOwner owner, CustomInventory inventory) {
        if (inventory instanceof SelectionMenu) {
            return ((SelectionMenu) inventory).getPages(owner).totalPages() > inventory.getCurrentPage(owner);
        }

        if (inventory instanceof SavesMenu) {
            return ((SavesMenu) inventory).getPages(owner).totalPages() > inventory.getCurrentPage(owner);
        }

        return false;
    }

    @Override
    public void onClick(PetOwner owner, CustomInventory inventory) {
        if (inventory instanceof SelectionMenu) {
            SelectionMenu menu = (SelectionMenu)inventory;
            ObjectPager<PetTypeStorage> pages = menu.getPages(owner);
            int current = menu.getCurrentPage(owner);

            if (pages == null) return;

            if (pages.totalPages() > current) menu.open(owner, (current+1));
            return;
        }

        inventory.open(owner, inventory.getCurrentPage(owner)+1);
    }
}
