package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.HorseStyleType;

import java.util.ArrayList;
import java.util.List;

public class HorseStyle extends MenuItemAbstract {
    public HorseStyle(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public HorseStyle(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("horsestyle", 0);
        if (entityPet instanceof IEntityHorsePet) {
            IEntityHorsePet var = (IEntityHorsePet) entityPet;
            HorseStyleType typeID = HorseStyleType.NONE;
            if (var.getStyle() != null) {
                typeID = var.getStyle();
            }
            switch (typeID) {
                case NONE:
                    item = type.getDataItemByName("horsestyle", 0);
                    break;
                case BLACK_DOTS:
                    item = type.getDataItemByName("horsestyle", 1);
                    break;
                case WHITE_DOTS:
                    item = type.getDataItemByName("horsestyle", 2);
                    break;
                case WHITEFIELD:
                    item = type.getDataItemByName("horsestyle", 3);
                    break;
                case WHITE:
                    item = type.getDataItemByName("horsestyle", 4);
                    break;
            }
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        ItemBuilder item = new ItemBuilder(Material.LEASH);
        item.withName("&6None");
        items.add(item);
        item.withName("&6Black Spots");
        items.add(item);
        item.withName("&6White Dots");
        items.add(item);
        item.withName("&6White Field");
        items.add(item);
        item.withName("&6White");
        items.add(item);
        return items;
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
