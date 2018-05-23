package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityWitherPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WitherSize extends MenuItemAbstract {
    private ItemBuilder item = type.getDataItemByName("withersize", 0);
    public WitherSize(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public WitherSize(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        if (entityPet instanceof IEntityWitherPet) {
            IEntityWitherPet var = (IEntityWitherPet) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isSmall())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.NETHER_BRICK);
        item.withName("&6Small: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityWitherPet) {
            IEntityWitherPet pet = (IEntityWitherPet) entityPet;
            if (pet.isSmall()) {
                pet.setSmall(false);
            } else {
                pet.setSmall(true);
            }
        }
    }
}
