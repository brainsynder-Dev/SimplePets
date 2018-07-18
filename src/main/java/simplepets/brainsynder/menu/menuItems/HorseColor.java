package simplepets.brainsynder.menu.menuItems;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.utils.Utilities;
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
        ItemBuilder item = Utilities.getColoredMaterial(Utilities.MatType.WOOL, 15).toBuilder(1);
        item.withName("&6Black");
        item.withData(15);
        items.add(item);
        item = Utilities.getColoredMaterial(Utilities.MatType.STAINED_CLAY, 8).toBuilder(1);
        item.withName("&6Chestnut");
        item.withData(8);
        items.add(item);
        item = Utilities.getColoredMaterial(Utilities.MatType.WOOL, 4).toBuilder(1);
        item.withName("&6Creamy");
        item.withData(4);
        items.add(item);
        item = Utilities.getColoredMaterial(Utilities.MatType.WOOL, 12).toBuilder(1);
        item.withName("&6Brown");
        item.withData(12);
        items.add(item);
        item = Utilities.getColoredMaterial(Utilities.MatType.STAINED_CLAY, 7).toBuilder(1);
        item.withName("&6Dark Brown");
        item.withData(7);
        items.add(item);
        item = Utilities.getColoredMaterial(Utilities.MatType.WOOL, 7).toBuilder(1);
        item.withName("&6Gray");
        item.withData(7);
        items.add(item);
        item = Utilities.getColoredMaterial(Utilities.MatType.WOOL, 0).toBuilder(1);
        item.withName("&6White");
        items.add(item);
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
