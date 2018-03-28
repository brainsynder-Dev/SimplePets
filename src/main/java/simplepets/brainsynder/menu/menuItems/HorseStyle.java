package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.HorseStyleType;

public class HorseStyle extends MenuItemAbstract {
    public HorseStyle(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public HorseStyle(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = new ItemBuilder(Material.LEASH);
        if (entityPet instanceof IEntityHorsePet) {
            IEntityHorsePet var = (IEntityHorsePet) entityPet;
            HorseStyleType typeID = HorseStyleType.NONE;
            if (var.getStyle() != null) {
                typeID = var.getStyle();
            }
            switch (typeID) {
                case NONE:
                    item.withName("&6None");
                    break;
                case BLACK_DOTS:
                    item.withName("&6Black Spots");
                    break;
                case WHITE_DOTS:
                    item.withName("&6White Dots");
                    break;
                case WHITEFIELD:
                    item.withName("&6White Field");
                    break;
                case WHITE:
                    item.withName("&6White");
                    break;
            }
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = new ItemBuilder(Material.LEASH);
        if (entityPet instanceof IEntityHorsePet) {
            IEntityHorsePet var = (IEntityHorsePet) entityPet;
            HorseStyleType typeID = HorseStyleType.NONE;
            if (var.getStyle() != null) {
                typeID = var.getStyle();
            }
            switch (typeID) {
                case NONE:
                    item.withName("&6None");
                    break;
                case BLACK_DOTS:
                    item.withName("&6Black Spots");
                    break;
                case WHITE_DOTS:
                    item.withName("&6White Dots");
                    break;
                case WHITEFIELD:
                    item.withName("&6White Field");
                    break;
                case WHITE:
                    item.withName("&6White");
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
