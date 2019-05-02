package simplepets.brainsynder.menu.menuItems.base;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.pet.PetDefault;

public abstract class MenuItemAbstract<E extends IEntityPet> extends MenuItem<E> {

    public MenuItemAbstract(PetDefault type, IEntityPet entityPet) {
        super(type, (E) entityPet);
    }
    public MenuItemAbstract(PetDefault type) {
        super(type);
    }

    public abstract void onLeftClick();

    public void onRightClick() {
        onLeftClick();
    }
}
