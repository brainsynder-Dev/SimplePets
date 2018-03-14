package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntitySnowmanPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

public class Pumpkin extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.PUMPKIN);

    public Pumpkin(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Pumpkin(PetDefault type) {
        super(type);
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
