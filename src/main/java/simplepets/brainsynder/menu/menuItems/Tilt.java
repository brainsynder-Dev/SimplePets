package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityWolfPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tilt extends MenuItemAbstract {


    public Tilt(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Tilt(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("tilt", 0);
        if (entityPet instanceof IEntityWolfPet) {
            IEntityWolfPet var = (IEntityWolfPet) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isHeadTilted())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.SKELETON_SKULL);
        item.withName("&6Head Tilted: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityWolfPet) {
            IEntityWolfPet pet = (IEntityWolfPet) entityPet;
            if (pet.isHeadTilted()) {
                pet.setHeadTilted(false);
            } else {
                pet.setHeadTilted(true);
            }
        }
    }
}
