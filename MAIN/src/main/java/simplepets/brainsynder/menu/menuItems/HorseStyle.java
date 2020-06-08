package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;
import simplepets.brainsynder.wrapper.HorseStyleType;

import java.util.ArrayList;
import java.util.List;

@ValueType(def = "NONE", target = "https://github.com/brainsynder-Dev/SimplePets/blob/master/MAIN/src/main/java/simplepets/brainsynder/wrapper/HorseStyleType.java")
public class HorseStyle extends MenuItemAbstract {
    public HorseStyle(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public HorseStyle(PetType type) {
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
        ItemBuilder item = new ItemBuilder(Material.LEAD);
        item.withName("&6None");
        items.add(item.clone());
        item.withName("&6Black Spots");
        items.add(item.clone());
        item.withName("&6White Dots");
        items.add(item.clone());
        item.withName("&6White Field");
        items.add(item.clone());
        item.withName("&6White");
        items.add(item.clone());
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
