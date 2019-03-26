package simplepets.brainsynder.menu.menuItems;

import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.HorseColorType;

import java.util.ArrayList;
import java.util.List;

public class HorseColor extends MenuItemAbstract {
    public HorseColor(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public HorseColor(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = null;
        if (entityPet instanceof IEntityHorsePet) {
            IEntityHorsePet var = (IEntityHorsePet) entityPet;
            HorseColorType typeID = HorseColorType.WHITE;
            if (var.getColor() != null) {
                typeID = var.getColor();
            }
            switch (typeID) {
                case BLACK:
                    item = type.getDataItemByName("horsecolor", 0);
                    break;
                case CHESTNUT:
                    item = type.getDataItemByName("horsecolor", 1);
                    break;
                case CREAMY:
                    item = type.getDataItemByName("horsecolor", 2);
                    break;
                case BROWN:
                    item = type.getDataItemByName("horsecolor", 3);
                    break;
                case DARKBROWN:
                    item = type.getDataItemByName("horsecolor", 4);
                    break;
                case GRAY:
                    item = type.getDataItemByName("horsecolor", 5);
                    break;
                case WHITE:
                    item = type.getDataItemByName("horsecolor", 6);
                    break;
            }
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        ItemBuilder item = ItemBuilder.getColored(simple.brainsynder.utils.MatType.WOOL, 15);
        item.withName("&6Black");
        items.add(item.clone());
        item = ItemBuilder.getColored(simple.brainsynder.utils.MatType.STAINED_CLAY, 8);
        item.withName("&6Chestnut");
        items.add(item.clone());
        item = ItemBuilder.getColored(simple.brainsynder.utils.MatType.WOOL, 4);
        item.withName("&6Creamy");
        items.add(item.clone());
        item = ItemBuilder.getColored(simple.brainsynder.utils.MatType.WOOL, 12);
        item.withName("&6Brown");
        items.add(item.clone());
        item = ItemBuilder.getColored(simple.brainsynder.utils.MatType.STAINED_CLAY, 7);
        item.withName("&6Dark Brown");
        items.add(item.clone());
        item = ItemBuilder.getColored(simple.brainsynder.utils.MatType.WOOL, 7);
        item.withName("&6Gray");
        items.add(item.clone());
        item = ItemBuilder.getColored(simple.brainsynder.utils.MatType.WOOL, 0);
        item.withName("&6White");
        items.add(item.clone());
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityHorsePet) {
            IEntityHorsePet var = (IEntityHorsePet) entityPet;
            int typeID = 0;
            if (var.getColor() != null) {
                typeID = var.getColor().getId();
            }
            if (typeID == 6) {
                typeID = 0;
            } else {
                typeID++;
            }
            var.setColor(HorseColorType.getByID(typeID));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IEntityHorsePet) {
            IEntityHorsePet var = (IEntityHorsePet) entityPet;
            int typeID = 0;
            if (var.getColor() != null) {
                typeID = var.getColor().getId();
            }
            if (typeID == 0) {
                typeID = 6;
            } else {
                typeID--;
            }
            var.setColor(HorseColorType.getByID(typeID));
        }
    }
}
