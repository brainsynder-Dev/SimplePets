package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IHorseAbstract;
import simplepets.brainsynder.api.entity.misc.ISaddle;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Saddle extends MenuItemAbstract {

    public Saddle(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    public Saddle(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("saddle", 0);
        try {
            if (entityPet instanceof ISaddle) {
                ISaddle var = (ISaddle) entityPet;
                item.withName(item.getName().replace("%value%", String.valueOf(var.isSaddled())));
            } else if (entityPet instanceof IHorseAbstract) {
                IHorseAbstract var = (IHorseAbstract) entityPet;
                item.withName(item.getName().replace("%value%", String.valueOf(var.isSaddled())));
            }
        } catch (Exception e) {
            item.withName("&6Has Saddle: &cERROR");
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.SADDLE);
        item.withName("&6Has Saddle: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof ISaddle) {
            ISaddle var = (ISaddle) entityPet;
            var.setSaddled(!var.isSaddled());
        } else if (entityPet instanceof IHorseAbstract) {
            IHorseAbstract var = (IHorseAbstract) entityPet;
            var.setSaddled(!var.isSaddled());
        }
    }
}
