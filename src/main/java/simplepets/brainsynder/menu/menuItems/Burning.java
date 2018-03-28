package simplepets.brainsynder.menu.menuItems;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityBlazePet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

public class Burning extends MenuItemAbstract {
    private ItemBuilder item = type.getDataItemByName("burning");

    public Burning(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Burning(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        if (entityPet instanceof IEntityBlazePet) {
            IEntityBlazePet var = (IEntityBlazePet) entityPet;
            item.withName(String.valueOf(item.toJSON().get("name"))
                    .replace("%value%", String.valueOf(var.isBurning())));
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = this.item;
        if (item != null) {
            item.withName("&6Burning: &e%value%");
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityBlazePet) {
            IEntityBlazePet pet = (IEntityBlazePet) entityPet;
            if (pet.isBurning()) {
                pet.setBurning(false);
            } else {
                pet.setBurning(true);
            }
        }
    }
}
