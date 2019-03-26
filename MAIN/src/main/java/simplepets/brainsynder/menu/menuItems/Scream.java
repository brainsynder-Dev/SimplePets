package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityEndermanPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scream extends MenuItemAbstract {


    public Scream(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Scream(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("scream", 0);
        if (entityPet instanceof IEntityEndermanPet) {
            IEntityEndermanPet var = (IEntityEndermanPet) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isScreaming())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.ENDER_PEARL);
        item.withName("&6Screaming: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityEndermanPet) {
            IEntityEndermanPet pet = (IEntityEndermanPet) entityPet;
            if (pet.isScreaming()) {
                pet.setScreaming(false);
            } else {
                pet.setScreaming(true);
            }
        }
    }
}
