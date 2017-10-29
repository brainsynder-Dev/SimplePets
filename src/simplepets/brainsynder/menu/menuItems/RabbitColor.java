package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntityRabbitPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.RabbitType;

public class RabbitColor extends MenuItemAbstract {
    public RabbitColor(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        ItemMaker item = null;
        if (entityPet instanceof IEntityRabbitPet) {
            IEntityRabbitPet var = (IEntityRabbitPet) entityPet;
            RabbitType typeID = RabbitType.WHITE;
            if (var.getRabbitType() != null) {
                typeID = var.getRabbitType();
            }
            switch (typeID) {
                case BLACK:
                    item = new ItemMaker(Material.WOOL, (byte) 15);
                    item.setName("&6Black");
                    break;
                case BROWN:
                    item = new ItemMaker(Material.WOOL, (byte) 12);
                    item.setName("&6Brown");
                    break;
                case GOLD:
                    item = new ItemMaker(Material.GOLD_BLOCK);
                    item.setName("&6Golden");
                    break;
                case BLACK_AND_WHITE:
                    item = new ItemMaker(Material.WOOL, (byte) 0);
                    item.setName("&6Black and White");
                    break;
                case SALT_AND_PEPPER:
                    item = new ItemMaker(Material.WOOL, (byte) 7);
                    item.setName("&6Salt and Pepper");
                    break;
                case THE_KILLER_BUNNY:
                    item = new ItemMaker(Material.REDSTONE_BLOCK);
                    item.setName("&6Killer Bunny");
                    break;
                case WHITE:
                    item = new ItemMaker(Material.QUARTZ_BLOCK);
                    item.setName("&6White");
                    break;
            }
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityRabbitPet) {
            IEntityRabbitPet var = (IEntityRabbitPet) entityPet;
            int typeID = 0;
            if (var.getRabbitType() != null) {
                typeID = var.getRabbitType().getId();
            }
            if (typeID == 5) {
                typeID = 99;
            } else if (typeID == 99) {
                typeID = 0;
            } else {
                typeID++;
            }
            var.setRabbitType(RabbitType.getByID(typeID));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IEntityRabbitPet) {
            IEntityRabbitPet var = (IEntityRabbitPet) entityPet;
            int typeID = 0;
            if (var.getRabbitType() != null) {
                typeID = var.getRabbitType().getId();
            }
            if (typeID == 99) {
                typeID = 5;
            } else if (typeID == 0) {
                typeID = 99;
            } else {
                typeID--;
            }
            var.setRabbitType(RabbitType.getByID(typeID));
        }
    }
}
