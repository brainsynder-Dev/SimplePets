package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.nms.entities.type.main.ITameable;
import simplepets.brainsynder.pet.PetType;

public class Sitting extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.WOOD_STAIRS);

    public Sitting(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
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
