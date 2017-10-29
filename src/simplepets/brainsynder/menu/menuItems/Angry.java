package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntityWolfPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public class Angry extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.WOOL, (byte) 14);

    public Angry(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof IEntityWolfPet) {
            IEntityWolfPet var = (IEntityWolfPet) entityPet;
            item.setName("&6Angry: &e" + var.isAngry());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityWolfPet) {
            IEntityWolfPet pet = (IEntityWolfPet) entityPet;
            if (pet.isAngry()) {
                pet.setAngry(false);
            } else {
                pet.setAngry(true);
            }
        }
    }
}
