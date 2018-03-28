package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityVindicatorPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

public class Johnny extends MenuItemAbstract {
    private ItemBuilder item = type.getDataItemByName("johnny");
    public Johnny(PetDefault type) {
        super(type);
    }

    public Johnny(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemBuilder getItem() {
        if (entityPet instanceof IEntityVindicatorPet) {
            IEntityVindicatorPet var = (IEntityVindicatorPet) entityPet;
            item.withName(String.valueOf(item.toJSON().get("name")).replace("%value%", String.valueOf(var.isJohnny())));
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = new ItemBuilder(Material.IRON_AXE);
        if (entityPet instanceof IEntityVindicatorPet) {
            IEntityVindicatorPet var = (IEntityVindicatorPet) entityPet;
            item.withName("&6Johnny: &e" + var.isJohnny());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityVindicatorPet) {
            IEntityVindicatorPet pet = (IEntityVindicatorPet) entityPet;
            if (pet.isJohnny()) {
                pet.setJohnny(false);
            } else {
                pet.setJohnny(true);
            }
        }
    }
}
