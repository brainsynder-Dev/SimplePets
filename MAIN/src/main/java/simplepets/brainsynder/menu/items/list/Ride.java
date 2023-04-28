package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.pet.IPetConfig;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.menu.inventory.PetSelectorMenu;

import java.io.File;

@Namespace(namespace = "ride")
public class Ride extends Item {
    public Ride(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.DIAMOND_HORSE_ARMOR).withName("&#e3c79a&lRide Your Pet");
    }

    @Override
    public boolean addItemToInv(PetUser user, CustomInventory inventory) {
        for (IEntityPet entity : user.getPetEntities()) {
            IPetConfig config = SimplePets.getPetConfigManager().getPetConfig(entity.getPetType()).orElse(null);
            if (config == null) continue;
            if (config.canMount(user.getPlayer())) return true;
        }
        return ConfigOption.INSTANCE.PET_TOGGLES_MOUNTABLE.getValue();
    }

    @Override
    public void onClick(PetUser masterUser, CustomInventory inventory, IEntityPet pet) {
        if (!masterUser.hasPets()) return;

        if (pet != null) {
            if (ConfigOption.INSTANCE.MISC_TOGGLES_AUTO_CLOSE_RIDE.getValue())
                masterUser.getPlayer().closeInventory();
            new BukkitRunnable() {
                @Override
                public void run() {
                    masterUser.setPetVehicle(pet.getPetType(), !masterUser.isPetVehicle(pet.getPetType()));
                }
            }.runTaskLater(PetCore.getInstance(), 2);
            return;
        }

        if (masterUser.getPetEntities().size() == 1) {
            if (ConfigOption.INSTANCE.MISC_TOGGLES_AUTO_CLOSE_RIDE.getValue())
                masterUser.getPlayer().closeInventory();
            new BukkitRunnable() {
                @Override
                public void run() {
                    masterUser.getPetEntities().stream().findFirst().ifPresent(iEntityPet -> {
                        masterUser.setPetVehicle(iEntityPet.getPetType(), !masterUser.isPetVehicle(iEntityPet.getPetType()));
                    });
                }
            }.runTaskLater(PetCore.getInstance(), 2);
            return;
        }
        PetSelectorMenu menu = InventoryManager.SELECTOR;
        menu.setTask(masterUser.getPlayer().getName(), (user, type) -> {
            if (ConfigOption.INSTANCE.MISC_TOGGLES_AUTO_CLOSE_RIDE.getValue())
                user.getPlayer().closeInventory();
            new BukkitRunnable() {
                @Override
                public void run() {
                    user.setPetVehicle(type, !user.isPetVehicle(type));
                }
            }.runTaskLater(PetCore.getInstance(), 2);
        });
        menu.open(masterUser, 1, inventory.getTitle());
    }
}
