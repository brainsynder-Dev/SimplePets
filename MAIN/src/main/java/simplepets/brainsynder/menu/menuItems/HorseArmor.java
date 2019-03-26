package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.HorseArmorType;

import java.util.ArrayList;
import java.util.List;

public class HorseArmor extends MenuItemAbstract {
    public HorseArmor(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public HorseArmor(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = null;
        if (entityPet instanceof IEntityHorsePet) {
            IEntityHorsePet var = (IEntityHorsePet) entityPet;
            int typeID = 0;
            if (var.getArmor() != null) {
                typeID = var.getArmor().getId();
            }
            switch (typeID) {
                case 0:
                    item = type.getDataItemByName("horsearmor", 0);
                    break;
                case 1:
                    item = type.getDataItemByName("horsearmor", 1);
                    break;
                case 2:
                    item = type.getDataItemByName("horsearmor", 2);
                    break;
                case 3:
                    item = type.getDataItemByName("horsearmor", 3);
                    break;
            }
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        ItemBuilder item = new ItemBuilder(Material.BARRIER);
        item.withName("&6No HorseArmor");
        items.add(item);
        item = new ItemBuilder(Utilities.fetchMaterial("IRON_BARDING", "IRON_HORSE_ARMOR"));
        item.withName("&6Iron HorseArmor");
        items.add(item);
        item = new ItemBuilder(Utilities.fetchMaterial("GOLD_BARDING", "GOLDEN_HORSE_ARMOR"));
        item.withName("&6Gold HorseArmor");
        items.add(item);
        item = new ItemBuilder(Utilities.fetchMaterial("DIAMOND_BARDING", "DIAMOND_HORSE_ARMOR"));
        item.withName("&6Diamond HorseArmor");
        items.add(item);
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityHorsePet) {
            IEntityHorsePet var = (IEntityHorsePet) entityPet;
            int typeID = 0;
            if (var.getArmor() != null) {
                typeID = var.getArmor().getId();
            }
            if (typeID == 3) {
                typeID = 0;
            } else {
                typeID++;
            }
            var.setArmor(HorseArmorType.fromId(typeID));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IEntityHorsePet) {
            IEntityHorsePet var = (IEntityHorsePet) entityPet;
            int typeID = 0;
            if (var.getArmor() != null) {
                typeID = var.getArmor().getId();
            }
            if (typeID == 0) {
                typeID = 3;
            } else {
                typeID--;
            }
            var.setArmor(HorseArmorType.fromId(typeID));
        }
    }
}
