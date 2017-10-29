package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntityWolfPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public class Tilt extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.SKULL_ITEM);

    public Tilt(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
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
