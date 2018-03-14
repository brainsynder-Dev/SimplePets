package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntityVindicatorPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public class Johnny extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.IRON_AXE);

    public Johnny(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof IEntityVindicatorPet) {
            IEntityVindicatorPet var = (IEntityVindicatorPet) entityPet;
            item.setName("&6Johnny: &e" + var.isJohnny());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityVindicatorPet) {
            IEntityVindicatorPet pet = (IEntityVindicatorPet) entityPet;
            if (pet.isJohnny()) {
                pet.setJohnny(false);
            } else {
                pet.setJohnny(true);
            }
        }
    }
}
