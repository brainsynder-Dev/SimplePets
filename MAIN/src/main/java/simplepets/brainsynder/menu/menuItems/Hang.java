package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityBatPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hang extends MenuItemAbstract {


    public Hang(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Hang(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("hang", 0);
        if (entityPet instanceof IEntityBatPet) {
            IEntityBatPet var = (IEntityBatPet) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isHanging())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.FEATHER);
        item.withName("&6Hanging: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
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
