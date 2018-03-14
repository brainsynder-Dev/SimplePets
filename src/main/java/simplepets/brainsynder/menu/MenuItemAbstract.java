package simplepets.brainsynder.menu;

import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public abstract class MenuItemAbstract extends MenuItem {

    public MenuItemAbstract(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    public abstract void onLeftClick();

    public void onRightClick() {
        onLeftClick();
    }
}
