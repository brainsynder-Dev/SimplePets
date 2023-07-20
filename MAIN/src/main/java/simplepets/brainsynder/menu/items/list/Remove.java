package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.menu.inventory.DataMenu;
import simplepets.brainsynder.menu.inventory.PetSelectorMenu;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Namespace(namespace = "remove")
public class Remove extends Item {

    public Remove(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#b35349&lRemove Pets").addLore("&#d1c9c9Click Here to remove this pet", "&#d1c9c9... or all pets if you have the selector screen open.", "&#d1c9c9Shift+Click to select a pet")
                .setTexture("http://textures.minecraft.net/texture/beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7");
    }

    @Override
    public void onClick(PetUser user, CustomInventory inventory, IEntityPet pet) {
        if (!user.hasPets()) return;
        if (!ConfigOption.INSTANCE.MISC_TOGGLES_REMOVE_ALL_PETS.getValue()) {
            onShiftClick(user, inventory, pet);
            return;
        }
        if (pet != null) {
            user.removePet(pet.getPetType());
            if (inventory instanceof DataMenu) user.updateDataMenu();
            return;
        }
        user.removePets();
        if (inventory instanceof DataMenu) user.updateDataMenu();
    }

    @Override
    public void onShiftClick(PetUser masterUser, CustomInventory inventory) {
        if (!masterUser.hasPets()) return;
        if (masterUser.getPetEntities().size() == 1) {
            if (ConfigOption.INSTANCE.MISC_TOGGLES_AUTO_CLOSE_REMOVE.getValue())
                masterUser.getPlayer().closeInventory();
            PetCore.getInstance().getScheduler().getImpl().runAtEntityLater(masterUser.getPlayer(), () -> masterUser.getPetEntities().stream().findFirst().ifPresent(iEntityPet -> {
                masterUser.removePet(iEntityPet.getPetType());
                if (inventory instanceof DataMenu) masterUser.updateDataMenu();
            }), 100L, TimeUnit.MILLISECONDS);
            return;
        }
        PetSelectorMenu menu = InventoryManager.SELECTOR;
        menu.setTask(masterUser.getPlayer().getName(), (user, type) -> {
            if (ConfigOption.INSTANCE.MISC_TOGGLES_AUTO_CLOSE_REMOVE.getValue())
                user.getPlayer().closeInventory();
            PetCore.getInstance().getScheduler().getImpl().runAtEntityLater(user.getPlayer(), () -> {
                user.removePet(type);
                if (inventory instanceof DataMenu) user.updateDataMenu();
            }, 100L, TimeUnit.MILLISECONDS);
        });
        menu.open(masterUser, 1, inventory.getTitle());
    }
}
