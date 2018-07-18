package simplepets.brainsynder.menu.menuItems;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityOcelotPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

public class Ocelot extends MenuItemAbstract {

    public Ocelot(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Ocelot(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("ocelot", 0);
        if (entityPet instanceof IEntityOcelotPet) {
            IEntityOcelotPet var = (IEntityOcelotPet) entityPet;
            int typeID = var.getCatType();
            switch (typeID) {
                case 0:
                    item = type.getDataItemByName("ocelot", 0);
                    break;
                case 1:
                    item = type.getDataItemByName("ocelot", 1);
                    break;
                case 2:
                    item = type.getDataItemByName("ocelot", 2);
                    break;
                case 3:
                    item = type.getDataItemByName("ocelot", 3);
                    break;
            }
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        ItemBuilder item = Utilities.getColoredMaterial(Utilities.MatType.INK_SACK, 11).toBuilder(1);
        item.withName("&6Wild Cat");
        items.add(item);
        item = Utilities.getColoredMaterial(Utilities.MatType.INK_SACK, 0).toBuilder(1);
        item.withName("&6Black Cat");
        items.add(item);
        item = Utilities.getColoredMaterial(Utilities.MatType.INK_SACK, 14).toBuilder(1);
        item.withName("&6Orange Cat");
        items.add(item);
        item = Utilities.getColoredMaterial(Utilities.MatType.INK_SACK, 7).toBuilder(1);
        item.withName("&6Siamese Cat");
        items.add(item);
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityOcelotPet) {
            IEntityOcelotPet var = (IEntityOcelotPet) entityPet;
            int typeID = var.getCatType();
            if (typeID == 3) {
                typeID = 0;
            } else {
                typeID++;
            }
            var.setCatType(typeID);
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IEntityOcelotPet) {
            IEntityOcelotPet var = (IEntityOcelotPet) entityPet;
            int typeID = var.getCatType();
            if (typeID == 0) {
                typeID = 3;
            } else {
                typeID--;
            }
            var.setCatType(typeID);
        }
    }
}
