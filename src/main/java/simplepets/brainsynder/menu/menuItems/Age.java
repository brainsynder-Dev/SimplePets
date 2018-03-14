package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IAgeablePet;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

public class Age extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.WHEAT);

    public Age(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Age(PetDefault type) {
        super(type);
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
