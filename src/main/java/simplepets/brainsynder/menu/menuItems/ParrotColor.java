package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityParrotPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.ParrotVariant;

public class ParrotColor extends MenuItemAbstract {

    public ParrotColor(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public ParrotColor(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = null;
        if (entityPet instanceof IEntityParrotPet) {
            IEntityParrotPet var = (IEntityParrotPet) entityPet;
            ParrotVariant typeID = var.getVariant();
            switch (typeID) {
                case BLUE:
                    item = new ItemBuilder(Material.WOOL, (byte) 11);
                    item.withName("&9Blue");
                    break;
                case CYAN:
                    item = new ItemBuilder(Material.WOOL, (byte) 9);
                    item.withName("&3Cyan");
                    break;
                case GRAY:
                    item = new ItemBuilder(Material.WOOL, (byte) 8);
                    item.withName("&7Gray");
                    break;
                case GREEN:
                    item = new ItemBuilder(Material.WOOL, (byte) 13);
                    item.withName("&2Green");
                    break;
                case RED:
                    item = new ItemBuilder(Material.WOOL, (byte) 14);
                    item.withName("&cRed");
                    break;
            }
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = null;
        if (entityPet instanceof IEntityParrotPet) {
            IEntityParrotPet var = (IEntityParrotPet) entityPet;
            ParrotVariant typeID = var.getVariant();
            switch (typeID) {
                case BLUE:
                    item = new ItemBuilder(Material.WOOL, (byte) 11);
                    item.withName("&9Blue");
                    break;
                case CYAN:
                    item = new ItemBuilder(Material.WOOL, (byte) 9);
                    item.withName("&3Cyan");
                    break;
                case GRAY:
                    item = new ItemBuilder(Material.WOOL, (byte) 8);
                    item.withName("&7Gray");
                    break;
                case GREEN:
                    item = new ItemBuilder(Material.WOOL, (byte) 13);
                    item.withName("&2Green");
                    break;
                case RED:
                    item = new ItemBuilder(Material.WOOL, (byte) 14);
                    item.withName("&cRed");
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
