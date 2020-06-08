package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityRabbitPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;
import simplepets.brainsynder.wrapper.RabbitType;

import java.util.ArrayList;
import java.util.List;

@ValueType(def = "BROWN", target = "https://github.com/brainsynder-Dev/SimplePets/blob/master/MAIN/src/main/java/simplepets/brainsynder/wrapper/RabbitType.java")
public class RabbitColor extends MenuItemAbstract {

    public RabbitColor(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public RabbitColor(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("rabbitcolor", 0);
        if (entityPet instanceof IEntityRabbitPet) {
            IEntityRabbitPet var = (IEntityRabbitPet) entityPet;
            RabbitType typeID = RabbitType.WHITE;
            if (var.getRabbitType() != null) {
                typeID = var.getRabbitType();
            }
            switch (typeID) {
                case BLACK:
                    item = type.getDataItemByName("rabbitcolor", 0);
                    break;
                case BROWN:
                    item = type.getDataItemByName("rabbitcolor", 1);
                    break;
                case GOLD:
                    item = type.getDataItemByName("rabbitcolor", 2);
                    break;
                case BLACK_AND_WHITE:
                    item = type.getDataItemByName("rabbitcolor", 3);
                    break;
                case SALT_AND_PEPPER:
                    item = type.getDataItemByName("rabbitcolor", 4);
                    break;
                case THE_KILLER_BUNNY:
                    item = type.getDataItemByName("rabbitcolor", 5);
                    break;
                case WHITE:
                    item = type.getDataItemByName("rabbitcolor", 6);
                    break;
            }
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        ItemBuilder item = lib.brainsynder.nms.DataConverter.getColoredMaterial(lib.brainsynder.nms.DataConverter.MaterialType.WOOL, 15);
        item.withName("&6Black");
        items.add(item);
        item = lib.brainsynder.nms.DataConverter.getColoredMaterial(lib.brainsynder.nms.DataConverter.MaterialType.WOOL, 12);
        item.withName("&6Brown");
        items.add(item);
        item = new ItemBuilder(Material.GOLD_BLOCK);
        item.withName("&6Golden");
        items.add(item);
        item = new ItemBuilder(Material.WHITE_WOOL, 0);
        item.withName("&6Black and White");
        items.add(item);
        item = lib.brainsynder.nms.DataConverter.getColoredMaterial(lib.brainsynder.nms.DataConverter.MaterialType.WOOL, 7);
        item.withName("&6Salt and Pepper");
        items.add(item);
        item = new ItemBuilder(Material.REDSTONE_BLOCK);
        item.withName("&6Killer Bunny");
        items.add(item);
        item = new ItemBuilder(Material.QUARTZ_BLOCK);
        item.withName("&6White");
        items.add(item);
        return items;
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
