package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IHorseAbstract;
import simplepets.brainsynder.api.entity.passive.IEntityPigPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Saddle extends MenuItemAbstract {

    public Saddle(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    public Saddle(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("saddle", 0);
        try {
            if (entityPet instanceof IEntityPigPet) {
                IEntityPigPet pig = (IEntityPigPet) entityPet;
                item.withName(item.getName().replace("%value%", String.valueOf(pig.hasSaddle())));
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
        if (entityPet instanceof IEntityPigPet) {
            IEntityPigPet pig = (IEntityPigPet) entityPet;
            pig.setSaddled(!pig.hasSaddle());
        } else if (entityPet instanceof IHorseAbstract) {
            IHorseAbstract var = (IHorseAbstract) entityPet;
            var.setSaddled(!var.isSaddled());
        }
    }
}
