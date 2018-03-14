package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ITameable;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

public class Tame extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.BONE);

    public Tame(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Tame(PetDefault type) {
        super(type);
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof ITameable) {
            ITameable var = (ITameable) entityPet;
            item.setName("&6Tamed: &e" + var.isTamed());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof ITameable) {
            ITameable pet = (ITameable) entityPet;
            if (pet.isTamed()) {
                pet.setTamed(false);
            } else {
                pet.setTamed(true);
            }
        }
    }
}
