package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IHorseAbstract;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Eating extends MenuItemAbstract {

    public Eating(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Eating(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName(getTargetName(), 0);
        if (item != null) {
            if (entityPet instanceof IHorseAbstract) {
                IHorseAbstract var = (IHorseAbstract) entityPet;
                item.withName(item.getName().replace("%value%", PetCore.get().getMessages().getTrueOrFalse(var.isEating())));
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
