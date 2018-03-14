package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntityLlamaPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public class LlamaColor extends MenuItemAbstract {
    public LlamaColor(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        ItemMaker item = null;
        if (entityPet instanceof IEntityLlamaPet) {
            IEntityLlamaPet var = (IEntityLlamaPet) entityPet;
            simplepets.brainsynder.wrapper.LlamaColor typeID = simplepets.brainsynder.wrapper.LlamaColor.CREAMY;
            if (var.getLlamaColor() != null) {
                typeID = var.getLlamaColor();
            }
            switch (typeID) {
                case CREAMY:
                    item = new ItemMaker(Material.WOOL, (byte) 4);
                    item.setName("&6Creamy");
                    break;
                case BROWN:
                    item = new ItemMaker(Material.WOOL, (byte) 12);
                    item.setName("&6Brown");
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
        if (entityPet instanceof IEntityLlamaPet) {
            IEntityLlamaPet var = (IEntityLlamaPet) entityPet;
            int typeID = 0;
            if (var.getLlamaColor() != null) {
                typeID = var.getLlamaColor().getId();
            }
            if (typeID == 3) {
                typeID = 0;
            } else {
                typeID++;
            }
            var.setSkinColor(simplepets.brainsynder.wrapper.LlamaColor.getByID(typeID));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IEntityLlamaPet) {
            IEntityLlamaPet var = (IEntityLlamaPet) entityPet;
            int typeID = 0;
            if (var.getLlamaColor() != null) {
                typeID = var.getLlamaColor().getId();
            }
            if (typeID == 0) {
                typeID = 3;
            } else {
                typeID--;
            }
            var.setSkinColor(simplepets.brainsynder.wrapper.LlamaColor.getByID(typeID));
        }
    }
}
