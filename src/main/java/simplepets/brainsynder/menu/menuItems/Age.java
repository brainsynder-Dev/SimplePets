package simplepets.brainsynder.menu.menuItems;

import simplepets.brainsynder.api.entity.IAgeablePet;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

public class Age extends MenuItemAbstract {
    private ItemBuilder item = type.getDataItemByName("age");

    public Age(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Age(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        if (item != null) {
            if (entityPet instanceof IAgeablePet) {
                IAgeablePet var = (IAgeablePet) entityPet;
                item.withName(String.valueOf(item.toJSON().get("name"))
                        .replace("%value%", String.valueOf(var.isBaby())));
            }
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = this.item;
        if (item != null) {
            item.withName("&6Baby: &e%value%");
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IAgeablePet) {
            IAgeablePet pet = (IAgeablePet) entityPet;
            if (pet.isBaby()) {
                pet.setBaby(false);
            } else {
                pet.setBaby(true);
            }
        }
    }
}
