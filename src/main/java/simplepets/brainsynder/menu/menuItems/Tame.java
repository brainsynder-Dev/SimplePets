package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.nms.entities.type.main.ITameable;
import simplepets.brainsynder.pet.PetType;

public class Tame extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.BONE);

    public Tame(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
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
