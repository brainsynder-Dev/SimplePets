package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.menu.inventory.PetSelectorMenu;

import java.io.File;

@Namespace(namespace = "remove")
public class Remove extends Item {

    public Remove(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#b35349&lRemove Pets").addLore("&#d1c9c9Click Here to remove all pets", "&#d1c9c9Shift+Click to select a pet")
                .setTexture("http://textures.minecraft.net/texture/beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7");
    }

    @Override
    public void onClick(PetUser user, CustomInventory inventory) {
        if (!user.hasPets()) return;
        user.removePets();
        user.updateDataMenu();
    }

    @Override
    public void onShiftClick(PetUser masterUser, CustomInventory inventory) {
        if (!masterUser.hasPets()) return;
        PetSelectorMenu menu = InventoryManager.SELECTOR;
        menu.setTask(masterUser.getPlayer().getName(), (user, type) -> {
            ((Player)user.getPlayer()).closeInventory();
            new BukkitRunnable() {
                @Override
                public void run() {
                    user.removePet(type);
                    user.updateDataMenu();
                }
            }.runTaskLater(PetCore.getInstance(), 2);
        });
        menu.open(masterUser, 1, inventory.getTitle());
    }
}
