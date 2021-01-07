package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.nbt.StorageTagCompound;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.inventory.list.SavesMenu;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.player.PetOwner;

import java.io.File;
import java.util.List;

public class SavePet extends Item {
    public SavePet(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.NAME_TAG)
                .withName("&e&lSave Pet")
                .addLore("&7Click here to save your current","&7pet for you to spawn later");
    }

    @Override
    public void onClick(PetOwner owner, CustomInventory inventory) {
        if (owner.hasPet()) {
            StorageTagCompound compound = owner.getPet().getVisableEntity().asCompound();
            if (!owner.containsPetSave(compound)) {
                List<StorageTagCompound> list = owner.getSavedPets();
                compound.setBoolean("restricted", true);
                list.add(compound);
                owner.setSavedPets(list);
                if (inventory instanceof SavesMenu) {
                    owner.getPlayer().closeInventory();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            inventory.open(owner);
                        }
                    }.runTaskLater(PetCore.get(), 1);
                }
            }
        }
    }
}
