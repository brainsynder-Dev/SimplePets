package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.IHorseAbstract;
import simplepets.brainsynder.api.entity.passive.IEntityPigPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

public class Saddle extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.SADDLE);

    public Saddle(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        try {
            if (entityPet instanceof IEntityPigPet) {
                IEntityPigPet pig = (IEntityPigPet) entityPet;
                item.setName("&6Has Saddle: &e" + pig.hasSaddle());
            } else if (entityPet instanceof IHorseAbstract) {
                IHorseAbstract var = (IHorseAbstract) entityPet;
                item.setName("&6Has Saddle: &e" + var.isSaddled());
            }
        } catch (Exception e) {
            item.setName("&6Has Saddle: &cERROR");
        }
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
