package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.HorseColorType;

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
                    item = new ItemBuilder(Material.WOOL, (byte) 15);
                    item.withName("&6Black");
                    break;
                case CHESTNUT:
                    item = new ItemBuilder(Material.STAINED_CLAY, (byte) 8);
                    item.withName("&6Chestnut");
                    break;
                case CREAMY:
                    item = new ItemBuilder(Material.WOOL, (byte) 4);
                    item.withName("&6Creamy");
                    break;
                case BROWN:
                    item = new ItemBuilder(Material.WOOL, (byte) 12);
                    item.withName("&6Brown");
                    break;
                case DARKBROWN:
                    item = new ItemBuilder(Material.STAINED_CLAY, (byte) 7);
                    item.withName("&6Dark Brown");
                    break;
                case GRAY:
                    item = new ItemBuilder(Material.WOOL, (byte) 7);
                    item.withName("&6Gray");
                    break;
                case WHITE:
                    item = new ItemBuilder(Material.WOOL);
                    item.withName("&6White");
                    break;
            }
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = null;
        if (entityPet instanceof IEntityHorsePet) {
            IEntityHorsePet var = (IEntityHorsePet) entityPet;
            HorseColorType typeID = HorseColorType.WHITE;
            if (var.getColor() != null) {
                typeID = var.getColor();
            }
            switch (typeID) {
                case BLACK:
                    item = new ItemBuilder(Material.WOOL, (byte) 15);
                    item.withName("&6Black");
                    break;
                case CHESTNUT:
                    item = new ItemBuilder(Material.STAINED_CLAY, (byte) 8);
                    item.withName("&6Chestnut");
                    break;
                case CREAMY:
                    item = new ItemBuilder(Material.WOOL, (byte) 4);
                    item.withName("&6Creamy");
                    break;
                case BROWN:
                    item = new ItemBuilder(Material.WOOL, (byte) 12);
                    item.withName("&6Brown");
                    break;
                case DARKBROWN:
                    item = new ItemBuilder(Material.STAINED_CLAY, (byte) 7);
                    item.withName("&6Dark Brown");
                    break;
                case GRAY:
                    item = new ItemBuilder(Material.WOOL, (byte) 7);
                    item.withName("&6Gray");
                    break;
                case WHITE:
                    item = new ItemBuilder(Material.WOOL);
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
