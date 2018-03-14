package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntitySnowmanPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public class Pumpkin extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.PUMPKIN);

    public Pumpkin(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public int getVersion() {
        return 19;
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof IEntitySnowmanPet) {
            IEntitySnowmanPet var = (IEntitySnowmanPet) entityPet;
            item.setName("&6Pumpkin: &e" + var.hasPumpkin());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntitySnowmanPet) {
            IEntitySnowmanPet var = (IEntitySnowmanPet) entityPet;
            if (var.hasPumpkin()) {
                var.setHasPumpkin(false);
            } else {
                var.setHasPumpkin(true);
            }
        }
    }
}
