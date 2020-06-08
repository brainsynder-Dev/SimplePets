package simplepets.brainsynder.menu.menuItems.base;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public abstract class MenuItemAbstract<E extends IEntityPet> extends MenuItem<E> {

    public MenuItemAbstract(PetType type, IEntityPet entityPet) {
        super(type, (E) entityPet);
    }
    public MenuItemAbstract(PetType type) {
        super(type);
    }

    public abstract void onLeftClick();

    public void onRightClick() {
        onLeftClick();
    }
}
