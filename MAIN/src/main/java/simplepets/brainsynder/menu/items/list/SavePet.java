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
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.menu.PetSelectorGUI;

import java.io.File;
import java.util.HashMap;

@Namespace(namespace = "savepet")
public class SavePet extends Item {
    public SavePet(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.CHAIN_COMMAND_BLOCK)
                .withName("&e&lSave Pet")
                .addLore("&7Click here to save your current","&7pet for you to spawn later");
    }

    @Override
    public void onClick(PetUser user, CustomInventory inventory) {
        if (!user.hasPets()) return;

        PetSelectorGUI gui = new PetSelectorGUI();
        gui.open(user, inventory, event -> {
            SimplePets.getUserManager().getPetUser(event.getPlayer()).ifPresent(user1 -> {
            PetType type = gui.getPageCache(user1).getOrDefault(event.getPage(), new HashMap<>()).getOrDefault(event.getPosition(), PetType.UNKNOWN);
                user1.getPetEntity(type).ifPresent(entity -> {
                    StorageTagCompound compound = entity.asCompound();
                    if (type == PetType.ARMOR_STAND) compound.setBoolean("restricted", true);
                    user1.addPetSave(compound);
                });
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        inventory.open(user1);
                    }
                }.runTaskLater(PetCore.getInstance(), 1);
            });
            event.setWillClose(true);
            event.setWillDestroy(true);
        });
    }
}