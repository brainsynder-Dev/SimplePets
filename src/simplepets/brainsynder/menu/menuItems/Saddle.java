package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntityPigPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.nms.entities.type.main.IHorseAbstract;
import simplepets.brainsynder.pet.PetType;

public class Saddle extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.SADDLE);

    public Saddle(PetType type, IEntityPet entityPet) {
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
