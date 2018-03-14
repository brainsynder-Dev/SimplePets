package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntityHorsePet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.HorseColorType;

public class HorseColor extends MenuItemAbstract {
    public HorseColor(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        ItemMaker item = null;
        if (entityPet instanceof IEntityHorsePet) {
            IEntityHorsePet var = (IEntityHorsePet) entityPet;
            HorseColorType typeID = HorseColorType.WHITE;
            if (var.getColor() != null) {
                typeID = var.getColor();
            }
            switch (typeID) {
                case BLACK:
                    item = new ItemMaker(Material.WOOL, (byte) 15);
                    item.setName("&6Black");
                    break;
                case CHESTNUT:
                    item = new ItemMaker(Material.STAINED_CLAY, (byte) 8);
                    item.setName("&6Chestnut");
                    break;
                case CREAMY:
                    item = new ItemMaker(Material.WOOL, (byte) 4);
                    item.setName("&6Creamy");
                    break;
                case BROWN:
                    item = new ItemMaker(Material.WOOL, (byte) 12);
                    item.setName("&6Brown");
                    break;
                case DARKBROWN:
                    item = new ItemMaker(Material.STAINED_CLAY, (byte) 7);
                    item.setName("&6Dark Brown");
                    break;
                case GRAY:
                    item = new ItemMaker(Material.WOOL, (byte) 7);
                    item.setName("&6Gray");
                    break;
                case WHITE:
                    item = new ItemMaker(Material.WOOL);
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
