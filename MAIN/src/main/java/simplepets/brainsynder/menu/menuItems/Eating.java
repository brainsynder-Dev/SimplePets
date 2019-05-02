package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IHorseAbstract;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Eating extends MenuItemAbstract {

    public Eating(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Eating(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName(getTargetName(), 0);
        if (item != null) {
            if (entityPet instanceof IHorseAbstract) {
                IHorseAbstract var = (IHorseAbstract) entityPet;
                item.withName(item.getName().replace("%value%", String.valueOf(var.isEating())));
            }
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.APPLE);
        item.withName("&6Eating: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IHorseAbstract) {
            IHorseAbstract var = (IHorseAbstract) entityPet;
            var.setEating(!var.isEating());
        }
    }

    @Override
    public String getTargetName() {
        return "eating";
    }
}
