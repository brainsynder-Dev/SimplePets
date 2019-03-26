package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Age extends MenuItemAbstract {
    public Age(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Age(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("age", 0);
        if (item != null) {
            if (entityPet instanceof IAgeablePet) {
                IAgeablePet var = (IAgeablePet) entityPet;
                item.withName(item.getName().replace("%value%", String.valueOf(var.isBaby())));
            }
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.WHEAT);
        item.withName("&6Baby: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
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
