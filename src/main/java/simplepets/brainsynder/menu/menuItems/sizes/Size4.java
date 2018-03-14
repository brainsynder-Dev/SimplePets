package simplepets.brainsynder.menu.menuItems.sizes;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityMagmaCubePet;
import simplepets.brainsynder.api.entity.hostile.IEntitySlimePet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

public class Size4 extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.SLIME_BLOCK);

    public Size4(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Size4(PetDefault type) {
        super(type);
    }

    @Override
    public ItemMaker getItem() {
        return item.setName("&6&lSize:&e " + 4);
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntitySlimePet) {
            IEntitySlimePet slime = (IEntitySlimePet) entityPet;
            slime.setSize(4);
        }
        if (entityPet instanceof IEntityMagmaCubePet) {
            IEntityMagmaCubePet slime = (IEntityMagmaCubePet) entityPet;
            slime.setSize(4);
        }
    }
}
