package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityLlamaPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

public class LlamaColor extends MenuItemAbstract {
    public LlamaColor(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public LlamaColor(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = null;
        if (entityPet instanceof IEntityLlamaPet) {
            IEntityLlamaPet var = (IEntityLlamaPet) entityPet;
            simplepets.brainsynder.wrapper.LlamaColor typeID = simplepets.brainsynder.wrapper.LlamaColor.CREAMY;
            if (var.getLlamaColor() != null) {
                typeID = var.getLlamaColor();
            }
            switch (typeID) {
                case CREAMY:
                    item = new ItemBuilder(Material.WOOL, (byte) 4);
                    item.withName("&6Creamy");
                    break;
                case BROWN:
                    item = new ItemBuilder(Material.WOOL, (byte) 12);
                    item.withName("&6Brown");
                    break;
                case GRAY:
                    item = new ItemBuilder(Material.WOOL, (byte) 7);
                    item.withName("&6Gray");
                    break;
                case WHITE:
                    item = new ItemBuilder(Material.WOOL);
                    item.withName("&6White");
                    break;
            }
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = null;
        if (entityPet instanceof IEntityLlamaPet) {
            IEntityLlamaPet var = (IEntityLlamaPet) entityPet;
            simplepets.brainsynder.wrapper.LlamaColor typeID = simplepets.brainsynder.wrapper.LlamaColor.CREAMY;
            if (var.getLlamaColor() != null) {
                typeID = var.getLlamaColor();
            }
            switch (typeID) {
                case CREAMY:
                    item = new ItemBuilder(Material.WOOL, (byte) 4);
                    item.withName("&6Creamy");
                    break;
                case BROWN:
                    item = new ItemBuilder(Material.WOOL, (byte) 12);
                    item.withName("&6Brown");
                    break;
                case GRAY:
                    item = new ItemBuilder(Material.WOOL, (byte) 7);
                    item.withName("&6Gray");
                    break;
                case WHITE:
                    item = new ItemBuilder(Material.WOOL);
                    item.withName("&6White");
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
