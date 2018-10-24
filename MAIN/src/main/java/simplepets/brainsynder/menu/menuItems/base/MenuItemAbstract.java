package simplepets.brainsynder.menu.menuItems.base;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.pet.PetDefault;

public abstract class MenuItemAbstract extends MenuItem {

    public MenuItemAbstract(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public MenuItemAbstract(PetDefault type) {
        super(type);
    }

    public abstract void onLeftClick();

    public void onRightClick() {
        onLeftClick();
    }
}
