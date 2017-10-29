package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntityEndermanPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public class Scream extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.ENDER_PEARL);

    public Scream(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
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
