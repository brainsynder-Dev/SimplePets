package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntityWitherPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public class WitherShield extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.NETHER_STAR);

    public WitherShield(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof IEntityWitherPet) {
            IEntityWitherPet var = (IEntityWitherPet) entityPet;
            item.setName("&6Shielded: &e" + var.isShielded());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityWitherPet) {
            IEntityWitherPet pet = (IEntityWitherPet) entityPet;
            if (pet.isShielded()) {
                pet.setShielded(false);
            } else {
                pet.setShielded(true);
            }
        }
    }
}
