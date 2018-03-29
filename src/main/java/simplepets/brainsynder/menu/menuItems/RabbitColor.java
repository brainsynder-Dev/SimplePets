package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityRabbitPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.RabbitType;

import java.util.ArrayList;
import java.util.List;

public class RabbitColor extends MenuItemAbstract {

    public RabbitColor(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public RabbitColor(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = null;
        if (entityPet instanceof IEntityRabbitPet) {
            IEntityRabbitPet var = (IEntityRabbitPet) entityPet;
            RabbitType typeID = RabbitType.WHITE;
            if (var.getRabbitType() != null) {
                typeID = var.getRabbitType();
            }
            switch (typeID) {
                case BLACK:
                    item = new ItemBuilder(Material.WOOL, (byte) 15);
                    item.withName("&6Black");
                    break;
                case BROWN:
                    item = new ItemBuilder(Material.WOOL, (byte) 12);
                    item.withName("&6Brown");
                    break;
                case GOLD:
                    item = new ItemBuilder(Material.GOLD_BLOCK);
                    item.withName("&6Golden");
                    break;
                case BLACK_AND_WHITE:
                    item = new ItemBuilder(Material.WOOL, (byte) 0);
                    item.withName("&6Black and White");
                    break;
                case SALT_AND_PEPPER:
                    item = new ItemBuilder(Material.WOOL, (byte) 7);
                    item.withName("&6Salt and Pepper");
                    break;
                case THE_KILLER_BUNNY:
                    item = new ItemBuilder(Material.REDSTONE_BLOCK);
                    item.withName("&6Killer Bunny");
                    break;
                case WHITE:
                    item = new ItemBuilder(Material.QUARTZ_BLOCK);
                    item.withName("&6White");
                    break;
            }
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = null;
        if (entityPet instanceof IEntityRabbitPet) {
            IEntityRabbitPet var = (IEntityRabbitPet) entityPet;
            RabbitType typeID = RabbitType.WHITE;
            if (var.getRabbitType() != null) {
                typeID = var.getRabbitType();
            }
            switch (typeID) {
                case BLACK:
                    item = new ItemBuilder(Material.WOOL, (byte) 15);
                    item.withName("&6Black");
                    break;
                case BROWN:
                    item = new ItemBuilder(Material.WOOL, (byte) 12);
                    item.withName("&6Brown");
                    break;
                case GOLD:
                    item = new ItemBuilder(Material.GOLD_BLOCK);
                    item.withName("&6Golden");
                    break;
                case BLACK_AND_WHITE:
                    item = new ItemBuilder(Material.WOOL, (byte) 0);
                    item.withName("&6Black and White");
                    break;
                case SALT_AND_PEPPER:
                    item = new ItemBuilder(Material.WOOL, (byte) 7);
                    item.withName("&6Salt and Pepper");
                    break;
                case THE_KILLER_BUNNY:
                    item = new ItemBuilder(Material.REDSTONE_BLOCK);
                    item.withName("&6Killer Bunny");
                    break;
                case WHITE:
                    item = new ItemBuilder(Material.QUARTZ_BLOCK);
                    item.withName("&6White");
                    break;
            }
        }
        return new ArrayList<>();
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
