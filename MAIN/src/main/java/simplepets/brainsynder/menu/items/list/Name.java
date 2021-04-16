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

@Namespace(namespace = "name")
public class Name extends Item {
    public Name(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.NAME_TAG).withName("&a&lName Pet");
    }

    @Override
    public boolean addItemToInv(PetUser owner, CustomInventory inventory) {
        return PetCore.getInstance().getConfiguration().getBoolean("RenamePet.Enabled");
    }

    @Override
    public void onClick(PetUser masterUser, CustomInventory inventory) {
        if (!masterUser.hasPets()) return;
        PetSelectorMenu menu = InventoryManager.SELECTOR;
        menu.setTask(masterUser.getPlayer().getName(), (user, type) -> {
            ((Player)user.getPlayer()).closeInventory();
            new BukkitRunnable() {
                @Override
                public void run() {
                    ((Player)user.getPlayer()).performCommand("pet rename "+type.getName());
                }
            }.runTaskLater(PetCore.getInstance(), 2);
        });
        menu.open(masterUser, 1, inventory.getTitle());
    }
}
