package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.main.IAgeablePet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public class Age extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.WHEAT);

    public Age(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof IAgeablePet) {
            IAgeablePet var = (IAgeablePet) entityPet;
            item.setName("&6Baby: &e" + var.isBaby());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IAgeablePet) {
            IAgeablePet pet = (IAgeablePet) entityPet;
            if (pet.isBaby()) {
                pet.setBaby(false);
            } else {
                pet.setBaby(true);
            }
        }
    }
}
