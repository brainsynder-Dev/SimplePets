package simplepets.brainsynder.menu.menuItems;

import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityZombieVillagerPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Shaking extends MenuItemAbstract {
    public Shaking(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Shaking(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("shaking", 0);
        if (entityPet instanceof IEntityZombieVillagerPet) {
            IEntityZombieVillagerPet var = (IEntityZombieVillagerPet) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isShaking())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Utilities.findMaterial("GOLDEN_APPLE"));
        item.withName("&6Shaking: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityZombieVillagerPet) {
            IEntityZombieVillagerPet var = (IEntityZombieVillagerPet) entityPet;
            if (var.isShaking()) {
                var.setShaking(false);
            } else {
                var.setShaking(true);
            }
        }
    }
}
