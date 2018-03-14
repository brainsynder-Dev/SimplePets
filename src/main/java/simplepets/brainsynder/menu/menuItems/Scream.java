package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityEndermanPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

public class Scream extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.ENDER_PEARL);

    public Scream(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Scream(PetDefault type) {
        super(type);
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof IEntityEndermanPet) {
            IEntityEndermanPet var = (IEntityEndermanPet) entityPet;
            item.setName("&6Screaming: &e" + var.isScreaming());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityEndermanPet) {
            IEntityEndermanPet pet = (IEntityEndermanPet) entityPet;
            if (pet.isScreaming()) {
                pet.setScreaming(false);
            } else {
                pet.setScreaming(true);
            }
        }
    }
}
