package simplepets.brainsynder.menu.menuItems.sizes;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityMagmaCubePet;
import simplepets.brainsynder.api.entity.hostile.IEntitySlimePet;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;

public class Size1 extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.SLIME_BLOCK);

    public Size1(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        return item.setName("&6&lSize:&e " + 1);
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntitySlimePet) {
            IEntitySlimePet slime = (IEntitySlimePet) entityPet;
            slime.setSize(1);
        }
        if (entityPet instanceof IEntityMagmaCubePet) {
            IEntityMagmaCubePet slime = (IEntityMagmaCubePet) entityPet;
            slime.setSize(1);
        }
    }
}