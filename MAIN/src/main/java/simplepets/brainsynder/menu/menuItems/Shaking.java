package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityZombiePet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
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
        if (entityPet instanceof IEntityZombiePet) {
            IEntityZombiePet var = (IEntityZombiePet) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isShaking())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.GOLDEN_APPLE);
        item.withName("&6Shaking: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityZombiePet) {
            IEntityZombiePet var = (IEntityZombiePet) entityPet;
            var.setShaking(!var.isShaking());
        }
    }
}
