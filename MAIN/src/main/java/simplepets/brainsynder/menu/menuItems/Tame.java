package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ITameable;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tame extends MenuItemAbstract {


    public Tame(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Tame(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("tame", 0);
        if (entityPet instanceof ITameable) {
            ITameable var = (ITameable) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isTamed())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.BONE);
        item.withName("&6Tamed: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof ITameable) {
            ITameable pet = (ITameable) entityPet;
            if (pet.isTamed()) {
                pet.setTamed(false);
            } else {
                pet.setTamed(true);
            }
        }
    }
}
