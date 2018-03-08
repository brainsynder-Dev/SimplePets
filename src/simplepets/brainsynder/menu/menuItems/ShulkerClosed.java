package simplepets.brainsynder.menu.menuItems;

import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityShulkerPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.MaterialWrapper;

public class ShulkerClosed extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(MaterialWrapper.SHULKER_SHELL.toMaterial());

    public ShulkerClosed(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        if (getEntityPet() instanceof IEntityShulkerPet) {
            IEntityShulkerPet var = (IEntityShulkerPet) getEntityPet();
            item.setName("&6Closed: &e" + var.isClosed());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityShulkerPet) {
            IEntityShulkerPet var = (IEntityShulkerPet) entityPet;
            var.setClosed(!var.isClosed());
        }
    }
}
