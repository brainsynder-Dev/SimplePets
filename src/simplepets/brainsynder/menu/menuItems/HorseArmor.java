package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntityHorsePet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.HorseArmorType;

public class HorseArmor extends MenuItemAbstract {
    public HorseArmor(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        ItemMaker item = null;
        if (entityPet instanceof IEntityHorsePet) {
            IEntityHorsePet var = (IEntityHorsePet) entityPet;
            int typeID = 0;
            if (var.getArmor() != null) {
                typeID = var.getArmor().getId();
            }
            switch (typeID) {
                case 0:
                    item = new ItemMaker(Material.BARRIER);
                    item.setName("&6No HorseArmor");
                    break;
                case 1:
                    item = new ItemMaker(Material.IRON_BARDING);
                    item.setName("&6Iron HorseArmor");
                    break;
                case 2:
                    item = new ItemMaker(Material.GOLD_BARDING);
                    item.setName("&6Gold HorseArmor");
                    break;
                case 3:
                    item = new ItemMaker(Material.DIAMOND_BARDING);
                    item.setName("&6Diamond HorseArmor");
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
