package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntitySheepPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public class Shear extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.SHEARS);

    public Shear(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
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
