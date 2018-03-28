package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.IHorseAbstract;
import simplepets.brainsynder.api.entity.passive.IEntityPigPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

public class Saddle extends MenuItemAbstract {
    private ItemBuilder item = type.getDataItemByName("saddle");

    public Saddle(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Saddle(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        try {
            if (entityPet instanceof IEntityPigPet) {
                IEntityPigPet pig = (IEntityPigPet) entityPet;
                item.withName(String.valueOf(item.toJSON().get("name"))
                .replace("%value%", String.valueOf(pig.hasSaddle())));
            } else if (entityPet instanceof IHorseAbstract) {
                IHorseAbstract var = (IHorseAbstract) entityPet;
                item.withName(String.valueOf(item.toJSON().get("name"))
                        .replace("%value%", String.valueOf(var.isSaddled())));
            }
        } catch (Exception e) {
            item.withName("&6Has Saddle: &cERROR");
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = new ItemBuilder(Material.SADDLE);
        item.withName("&6Has Saddle: &e%value%");
        return item;
    }

    @Override
    public void onLeftClick() {
        try {
            if (entityPet instanceof IEntityPigPet) {
                IEntityPigPet pig = (IEntityPigPet) entityPet;
                if (pig.hasSaddle()) {
                    pig.setSaddled(false);
                } else {
                    pig.setSaddled(true);
                }
            } else if (entityPet instanceof IHorseAbstract) {
                IHorseAbstract var = (IHorseAbstract) entityPet;
                if (var.isSaddled()) {
                    var.setSaddled(false);
                } else {
                    var.setSaddled(true);
                }
            }
        } catch (Exception e) {
        }
    }
}
