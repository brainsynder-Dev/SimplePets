package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityOcelotPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

public class Ocelot extends MenuItemAbstract {

    public Ocelot(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Ocelot(PetDefault type) {
        super(type);
    }

    @Override
    public ItemMaker getItem() {
        ItemMaker item = null;
        if (entityPet instanceof IEntityOcelotPet) {
            IEntityOcelotPet var = (IEntityOcelotPet) entityPet;
            int typeID = var.getCatType();
            switch (typeID) {
                case 0:
                    item = new ItemMaker(Material.INK_SACK, (byte) 11);
                    item.setName("&6Wild Cat");
                    break;
                case 1:
                    item = new ItemMaker(Material.INK_SACK, (byte) 0);
                    item.setName("&6Black Cat");
                    break;
                case 2:
                    item = new ItemMaker(Material.INK_SACK, (byte) 14);
                    item.setName("&6Orange Cat");
                    break;
                case 3:
                    item = new ItemMaker(Material.INK_SACK, (byte) 7);
                    item.setName("&6Siamese Cat");
                    break;
            }
        }
        return item;
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
