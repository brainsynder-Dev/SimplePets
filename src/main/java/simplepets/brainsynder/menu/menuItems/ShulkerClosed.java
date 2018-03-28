package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityShulkerPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

public class ShulkerClosed extends MenuItemAbstract {
    private ItemBuilder item = type.getDataItemByName("shulkerclosed");

    public ShulkerClosed(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public ShulkerClosed(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        if (getEntityPet() instanceof IEntityShulkerPet) {
            IEntityShulkerPet var = (IEntityShulkerPet) getEntityPet();
            item.withName(String.valueOf(item.toJSON().get("name")).replace("%value%", String.valueOf(var.isClosed())));
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = new ItemBuilder(Material.SHULKER_SHELL);
        item.withName("&6Closed: &e%value%");
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityShulkerPet) {
            IEntityShulkerPet var = (IEntityShulkerPet) entityPet;
            var.setClosed(!var.isClosed());
        }
    }
}
