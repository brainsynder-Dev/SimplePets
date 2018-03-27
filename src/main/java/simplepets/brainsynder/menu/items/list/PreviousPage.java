package simplepets.brainsynder.menu.items.list;

import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.inventory.list.DataMenu;
import simplepets.brainsynder.menu.inventory.list.SavesMenu;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.utils.ItemBuilder;

import java.io.File;

public class PreviousPage extends Item {
    public PreviousPage(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SKULL_ITEM)
                .withName("&6&l<&m----").withData(3)
                .addLore("&7Click Here to go", "&7the previous page")
                .setTexture("http://textures.minecraft.net/texture/f2599bd986659b8ce2c4988525c94e19ddd39fad08a38284a197f1b70675acc");
    }

    @Override
    public boolean addItemToInv(PetOwner owner, CustomInventory inventory) {
        if (inventory instanceof DataMenu) return true;
        if (inventory instanceof SavesMenu) {
            return inventory.getCurrentPage(owner) >= 1;
        }
        return inventory.getCurrentPage(owner) > 1;
    }

    @Override
    public void onClick(PetOwner owner, CustomInventory inventory) {
        if (inventory instanceof DataMenu) {
            PetCore.get().getInvLoaders().SELECTION.open(owner);
            return;
        }

        if (inventory instanceof SavesMenu) {
            if (inventory.getCurrentPage(owner) == 1) {
                PetCore.get().getInvLoaders().SELECTION.open(owner);
                return;
            }
        }

        int current = inventory.getCurrentPage(owner);
        if (current > 1) {
            inventory.open(owner, (current - 1));
        }
    }
}
