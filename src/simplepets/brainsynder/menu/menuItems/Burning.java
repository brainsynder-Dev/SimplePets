package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntityBlazePet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public class Burning extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.BLAZE_ROD);

    public Burning(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof IEntityBlazePet) {
            IEntityBlazePet var = (IEntityBlazePet) entityPet;
            item.setName("&6Burning: &e" + var.isBurning());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityBlazePet) {
            IEntityBlazePet age = (IEntityBlazePet) entityPet;
            if (age.isBurning()) {
                age.setBurning(false);
            } else {
                age.setBurning(true);
            }
        }
    }
}
