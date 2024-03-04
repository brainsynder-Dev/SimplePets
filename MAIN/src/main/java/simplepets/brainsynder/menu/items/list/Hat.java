package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
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
import java.util.concurrent.TimeUnit;

@Namespace(namespace = "hat")
public class Hat extends Item {
    public Hat(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.DIAMOND_HELMET).withName("&#e3c79a&lToggle Pet as Hat");
    }

    @Override
    public boolean addItemToInv(PetUser user, CustomInventory inventory) {
        for (IEntityPet entity : user.getPetEntities()) {
            IPetConfig config = SimplePets.getPetConfigManager().getPetConfig(entity.getPetType()).orElse(null);
            if (config == null) continue;
            if (config.canHat(user.getPlayer())) return true;
        }
        return ConfigOption.INSTANCE.PET_TOGGLES_HAT.getValue();
    }

    @Override
    public void onClick(PetUser masterUser, CustomInventory inventory, IEntityPet pet) {
        if (!masterUser.hasPets()) return;

        if (pet != null) {
            if (ConfigOption.INSTANCE.MISC_TOGGLES_AUTO_CLOSE_HAT.getValue())
                masterUser.getPlayer().closeInventory();
            PetCore.getInstance().getScheduler().getImpl().runAtEntityLater(masterUser.getPlayer(),
                () -> masterUser.setPetHat(pet.getPetType(), !masterUser.isPetHat(pet.getPetType())),
                100L, TimeUnit.MILLISECONDS
            );
            return;
        }

        if (masterUser.getPetEntities().size() == 1) {
            if (ConfigOption.INSTANCE.MISC_TOGGLES_AUTO_CLOSE_HAT.getValue())
                masterUser.getPlayer().closeInventory();
            PetCore.getInstance().getScheduler().getImpl().runAtEntityLater(masterUser.getPlayer(), () -> masterUser.getPetEntities().stream().findFirst().ifPresent(iEntityPet -> {
                masterUser.setPetHat(iEntityPet.getPetType(), !masterUser.isPetHat(iEntityPet.getPetType()));
            }), 100L, TimeUnit.MILLISECONDS);
            return;
        }

        PetSelectorMenu menu = InventoryManager.SELECTOR;
        menu.setTask(masterUser.getPlayer().getName(), (user, type) -> {
            if (ConfigOption.INSTANCE.MISC_TOGGLES_AUTO_CLOSE_HAT.getValue())
                user.getPlayer().closeInventory();
            PetCore.getInstance().getScheduler().getImpl().runAtEntityLater(user.getPlayer(), () -> user.setPetHat(type, !user.isPetHat(type)), 100L, TimeUnit.MILLISECONDS);
        });
        menu.open(masterUser, 1, inventory.getTitle());
    }
}
