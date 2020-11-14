package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntitySheepPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Shear extends MenuItemAbstract {


    public Shear(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Shear(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("shear", 0);
        if (entityPet instanceof IEntitySheepPet) {
            IEntitySheepPet var = (IEntitySheepPet) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isSheared())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.SHEARS);
        item.withName("&6Sheared: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntitySheepPet) {
            IEntitySheepPet pet = (IEntitySheepPet) entityPet;
            pet.setSheared(!pet.isSheared());
        }
    }
}
