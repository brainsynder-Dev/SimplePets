package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntityHorsePet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.HorseStyleType;

public class HorseStyle extends MenuItemAbstract {
    public HorseStyle(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        ItemMaker item = new ItemMaker(Material.LEASH);
        if (entityPet instanceof IEntityHorsePet) {
            IEntityHorsePet var = (IEntityHorsePet) entityPet;
            HorseStyleType typeID = HorseStyleType.NONE;
            if (var.getStyle() != null) {
                typeID = var.getStyle();
            }
            switch (typeID) {
                case NONE:
                    item.setName("&6None");
                    break;
                case BLACK_DOTS:
                    item.setName("&6Black Spots");
                    break;
                case WHITE_DOTS:
                    item.setName("&6White Dots");
                    break;
                case WHITEFIELD:
                    item.setName("&6White Field");
                    break;
                case WHITE:
                    item.setName("&6White");
                    break;
            }
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityHorsePet) {
            IEntityHorsePet var = (IEntityHorsePet) entityPet;
            int typeID = 0;
            if (var.getStyle() != null) {
                typeID = var.getStyle().getId();
            }
            if (typeID == 4) {
                typeID = 0;
            } else {
                typeID++;
            }
            var.setStyle(HorseStyleType.getByID(typeID));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IEntityHorsePet) {
            IEntityHorsePet var = (IEntityHorsePet) entityPet;
            int typeID = 0;
            if (var.getStyle() != null) {
                typeID = var.getStyle().getId();
            }
            if (typeID == 0) {
                typeID = 4;
            } else {
                typeID--;
            }
            var.setStyle(HorseStyleType.getByID(typeID));
        }
    }
}
