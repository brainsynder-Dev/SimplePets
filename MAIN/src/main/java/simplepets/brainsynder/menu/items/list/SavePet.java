package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.nbt.StorageTagCompound;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.menu.inventory.PetSelectorMenu;

import java.io.File;

@Namespace(namespace = "savepet")
public class SavePet extends Item {
    public SavePet(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.CHAIN_COMMAND_BLOCK)
                .withName("&#e3c79a&lSave Pet")
                .addLore("&7Click here to save your current","&7pet for you to spawn later");
    }

    @Override
    public void onClick(PetUser masterUser, CustomInventory inventory) {
        if (!masterUser.hasPets()) return;
        PetSelectorMenu menu = InventoryManager.SELECTOR;
        menu.setTask(masterUser.getPlayer().getName(), (user, type) -> {
            user.getPetEntity(type).ifPresent(entity -> {
                StorageTagCompound compound = entity.asCompound();
                if (type == PetType.ARMOR_STAND) compound.setBoolean("restricted", true);
                user.addPetSave(compound);
            });
            new BukkitRunnable() {
                @Override
                public void run() {
                    inventory.open(user);
                }
            }.runTaskLater(PetCore.getInstance(), 1);
        });
        menu.open(masterUser, 1, inventory.getTitle());
    }
}