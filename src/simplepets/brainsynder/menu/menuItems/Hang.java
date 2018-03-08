package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityBatPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

public class Hang extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.FEATHER);

    public Hang(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof IEntityBatPet) {
            IEntityBatPet var = (IEntityBatPet) entityPet;
            item.setName("&6Hanging: &e" + var.isHanging());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityBatPet) {
            IEntityBatPet bat = (IEntityBatPet) entityPet;
            if (bat.isHanging()) {
                bat.setHanging(false);
            } else {
                bat.setHanging(true);
            }
        }
    }
}
