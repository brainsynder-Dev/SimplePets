package simplepets.brainsynder.menu.menuItems;

import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityBatPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

public class Hang extends MenuItemAbstract {
    private ItemBuilder item = type.getDataItemByName("hang");

    public Hang(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Hang(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        if (entityPet instanceof IEntityBatPet) {
            IEntityBatPet var = (IEntityBatPet) entityPet;
            item.withName(String.valueOf(item.toJSON().get("name")));
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = this.item;
        if (item != null) {
            item.withName("&6Hanging: &e%value%");
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityBatPet) {
            IEntityBatPet bat = (IEntityBatPet) entityPet;
            if (bat.isHanging()) {
                bat.setHanging(false);
            } else {
                bat.setHanging(true);
            }
        }
    }
}
