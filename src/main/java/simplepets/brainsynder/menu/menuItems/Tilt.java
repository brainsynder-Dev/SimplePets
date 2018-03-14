package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityWolfPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

public class Tilt extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.SKULL_ITEM);

    public Tilt(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Tilt(PetDefault type) {
        super(type);
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof IEntityWolfPet) {
            IEntityWolfPet var = (IEntityWolfPet) entityPet;
            item.setName("&6Head Tilted: &e" + var.isHeadTilted());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityWolfPet) {
            IEntityWolfPet pet = (IEntityWolfPet) entityPet;
            if (pet.isHeadTilted()) {
                pet.setHeadTilted(false);
            } else {
                pet.setHeadTilted(true);
            }
        }
    }
}
