package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntityBatPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public class Hang extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.FEATHER);

    public Hang(PetType type, IEntityPet entityPet) {
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
