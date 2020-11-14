package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityWolfPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Angry extends MenuItemAbstract {

    public Angry(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Angry(PetType type) {
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
        ItemBuilder item = lib.brainsynder.nms.DataConverter.getColoredMaterial(lib.brainsynder.nms.DataConverter.MaterialType.WOOL, 14);
        item.withName("&6Angry: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityWolfPet) {
            IEntityWolfPet pet = (IEntityWolfPet) entityPet;
            pet.setAngry(!pet.isAngry());
        }
    }
}
