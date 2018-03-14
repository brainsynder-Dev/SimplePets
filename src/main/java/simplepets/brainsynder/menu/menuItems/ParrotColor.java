package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityParrotPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.ParrotVariant;

public class ParrotColor extends MenuItemAbstract {

    public ParrotColor(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public ParrotColor(PetDefault type) {
        super(type);
    }

    @Override
    public ItemMaker getItem() {
        ItemMaker item = null;
        if (entityPet instanceof IEntityParrotPet) {
            IEntityParrotPet var = (IEntityParrotPet) entityPet;
            ParrotVariant typeID = var.getVariant();
            switch (typeID) {
                case BLUE:
                    item = new ItemMaker(Material.WOOL, (byte) 11);
                    item.setName("&9Blue");
                    break;
                case CYAN:
                    item = new ItemMaker(Material.WOOL, (byte) 9);
                    item.setName("&3Cyan");
                    break;
                case GRAY:
                    item = new ItemMaker(Material.WOOL, (byte) 8);
                    item.setName("&7Gray");
                    break;
                case GREEN:
                    item = new ItemMaker(Material.WOOL, (byte) 13);
                    item.setName("&2Green");
                    break;
                case RED:
                    item = new ItemMaker(Material.WOOL, (byte) 14);
                    item.setName("&cRed");
                    break;
            }
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityParrotPet) {
            IEntityParrotPet var = (IEntityParrotPet) entityPet;

            ParrotVariant wrapper = ParrotVariant.RED;
            if (var.getVariant() != null)
                wrapper = var.getVariant();
            var.setVariant(ParrotVariant.getNext(wrapper));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IEntityParrotPet) {
            IEntityParrotPet var = (IEntityParrotPet) entityPet;
            ParrotVariant wrapper = ParrotVariant.RED;
            if (var.getVariant() != null)
                wrapper = var.getVariant();
            var.setVariant(ParrotVariant.getPrevious(wrapper));
        }
    }
}
