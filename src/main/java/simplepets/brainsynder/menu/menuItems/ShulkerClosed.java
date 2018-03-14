package simplepets.brainsynder.menu.menuItems;

import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntityShulkerPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.MaterialWrapper;

public class ShulkerClosed extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(MaterialWrapper.SHULKER_SHELL.toMaterial());

    public ShulkerClosed(PetType type, IEntityPet entityPet) {
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
