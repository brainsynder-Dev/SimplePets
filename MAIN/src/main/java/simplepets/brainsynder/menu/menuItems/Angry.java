package simplepets.brainsynder.menu.menuItems;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityWolfPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.utils.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Angry extends MenuItemAbstract {

    public Angry(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Angry(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("angry", 0);
        if (item != null) {
            if (entityPet instanceof IEntityWolfPet) {
                IEntityWolfPet var = (IEntityWolfPet) entityPet;
                item.withName(item.getName().replace("%value%", String.valueOf(var.isAngry())));

            }
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = Utilities.getColoredMaterial(Utilities.MatType.WOOL, 14).toBuilder(1);
        item.withName("&6Angry: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityWolfPet) {
            IEntityWolfPet pet = (IEntityWolfPet) entityPet;
            if (pet.isAngry()) {
                pet.setAngry(false);
            } else {
                pet.setAngry(true);
            }
        }
    }
}
