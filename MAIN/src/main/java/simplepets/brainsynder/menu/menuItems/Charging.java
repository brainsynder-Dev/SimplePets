package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityPiglinPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Charging extends MenuItemAbstract {
    public Charging(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Charging(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("charging", 0);
        if (item != null) {
            if (entityPet instanceof IEntityPiglinPet) {
                IEntityPiglinPet var = (IEntityPiglinPet) entityPet;
                item.withName(item.getName().replace("%value%", PetCore.get().getMessages().getTrueOrFalse(var.isCharging())));
            }
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.IRON_SWORD);
        item.withName("&6Charging: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityPiglinPet) {
            IEntityPiglinPet pet = (IEntityPiglinPet) entityPet;
            pet.setCharging(!pet.isCharging());
        }
    }
}
