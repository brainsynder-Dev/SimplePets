package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.storage.files.Messages;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Age extends MenuItemAbstract {
    public Age(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Age(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("age", 0);
        if (item != null) {
            if (entityPet instanceof IAgeablePet) {
                IAgeablePet var = (IAgeablePet) entityPet;
                item.withName(item.getName().replace("%value%", Messages.getTrueOrFalse(var.isBaby())));
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
            pet.setBaby(!pet.isBaby());
        }
    }
}
