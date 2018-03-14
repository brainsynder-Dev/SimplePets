package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntitySheepPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

public class Shear extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.SHEARS);

    public Shear(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Shear(PetDefault type) {
        super(type);
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof IEntitySheepPet) {
            IEntitySheepPet var = (IEntitySheepPet) entityPet;
            item.setName("&6Sheared: &e" + var.isSheared());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntitySheepPet) {
            IEntitySheepPet pet = (IEntitySheepPet) entityPet;
            if (pet.isSheared()) {
                pet.setSheared(false);
            } else {
                pet.setSheared(true);
            }
        }
    }
}
