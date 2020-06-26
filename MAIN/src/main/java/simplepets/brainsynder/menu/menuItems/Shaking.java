package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IShaking;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Shaking extends MenuItemAbstract {
    public Shaking(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Shaking(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("shaking", 0);
        if (entityPet instanceof IShaking) {
            IShaking var = (IShaking) entityPet;
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
        if (entityPet instanceof IShaking) {
            IShaking var = (IShaking) entityPet;
            var.setShaking(!var.isShaking());
        }
    }
}
