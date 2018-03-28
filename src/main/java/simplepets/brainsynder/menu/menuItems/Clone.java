package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

public class Clone extends MenuItemAbstract {
    private ItemBuilder item = type.getDataItemByName("clone");

    public Clone(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Clone(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        if (item != null) {
            if (entityPet instanceof IEntityArmorStandPet) {
                IEntityArmorStandPet var = (IEntityArmorStandPet) entityPet;
                item.withName(String.valueOf(item.toJSON().get("name"))
                .replace("%value%", String.valueOf(var.isOwner())));
            }
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = new ItemBuilder(Material.SKULL_ITEM, (byte) 3);
        item.withName("&6IsClone: &e%value%");
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityArmorStandPet) {
            IEntityArmorStandPet bat = (IEntityArmorStandPet) entityPet;
            if (bat.isOwner()) {
                bat.setOwner(false);
            } else {
                bat.setOwner(true);
            }
        }
    }
}
