package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ITameable;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

public class Sitting extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.WOOD_STAIRS);

    public Sitting(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Sitting(PetDefault type) {
        super(type);
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof ITameable) {
            ITameable var = (ITameable) entityPet;
            item.setName("&6Sitting: &e" + var.isSitting());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof ITameable) {
            ITameable var = (ITameable) entityPet;
            if (var.isSitting()) {
                var.setSitting(false);
            } else {
                var.setSitting(true);
            }
        }
    }
}
