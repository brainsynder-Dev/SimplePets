package simplepets.brainsynder.menu;

import org.json.simple.JSONObject;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public abstract class MenuItemAbstract extends MenuItem {

    public MenuItemAbstract(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    public static JSONObject toJSON() {
        return new JSONObject();
    }

    public abstract void onLeftClick();

    public void onRightClick() {
        onLeftClick();
    }


}
