package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityWitherPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

public class WitherSize extends MenuItemAbstract {
    public WitherSize(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        ItemMaker item = new ItemMaker(Material.NETHER_BRICK_ITEM);
        if (entityPet instanceof IEntityWitherPet) {
            IEntityWitherPet var = (IEntityWitherPet) entityPet;
            item.setName("&6Small: &e" + var.isSmall());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityWitherPet) {
            IEntityWitherPet pet = (IEntityWitherPet) entityPet;
            if (pet.isSmall()) {
                pet.setSmall(false);
            } else {
                pet.setSmall(true);
            }
        }
    }
}
