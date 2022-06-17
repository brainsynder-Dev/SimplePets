package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.menu.inventory.AddonMenu;

import java.io.File;

@Namespace(namespace = "installer")
public class Installer extends Item {
    public Installer(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/ce1f3cc63c73a6a1dde72fe09c6ac5569376d7b61231bb740764368788cbf1fa")
                .withName("&#e3c79aInstall Addons");
    }

    @Override
    public boolean addItemToInv(PetUser user, CustomInventory inventory) {
        return (inventory instanceof AddonMenu) && !((AddonMenu)inventory).isInstallerGUI(user); // Should only be added to the Addon GUI
    }

    @Override
    public void onClick(PetUser masterUser, CustomInventory inventory, IEntityPet pet) {
        InventoryManager.ADDONS.open(masterUser, 1, true);
    }
}
