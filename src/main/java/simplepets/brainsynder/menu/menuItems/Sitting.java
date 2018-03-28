package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ITameable;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

public class Sitting extends MenuItemAbstract {
    private ItemBuilder item = type.getDataItemByName("sitting");

    public Sitting(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Sitting(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        if (entityPet instanceof ITameable) {
            ITameable var = (ITameable) entityPet;
            item.withName(String.valueOf(item.toJSON().get("name")).replace("%value%", String.valueOf(var.isSitting())));
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = new ItemBuilder(Material.WOOD_STAIRS);
        item.withName("&6Sitting: &e%value%");
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof ITameable) {
            ITameable var = (ITameable) entityPet;
            if (var.isSitting()) {
                var.setSitting(false);
            } else {
                var.setSitting(true);
            }
        }
    }
}
