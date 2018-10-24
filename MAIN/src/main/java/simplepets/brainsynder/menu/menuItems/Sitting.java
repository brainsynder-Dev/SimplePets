package simplepets.brainsynder.menu.menuItems;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ITameable;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.utils.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sitting extends MenuItemAbstract {
    public Sitting(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Sitting(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("sitting", 0);
        if (entityPet instanceof ITameable) {
            ITameable var = (ITameable) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isSitting())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Utilities.fetchMaterial("WOOD_STAIRS", "OAK_STAIRS"));
        item.withName("&6Sitting: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
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
