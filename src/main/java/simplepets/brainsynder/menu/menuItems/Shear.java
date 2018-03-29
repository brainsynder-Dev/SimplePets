package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntitySheepPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

public class Shear extends MenuItemAbstract {


    public Shear(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Shear(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("shear", 0);
        if (entityPet instanceof IEntitySheepPet) {
            IEntitySheepPet var = (IEntitySheepPet) entityPet;
            item.withName(String.valueOf(item.toJSON().get("name")).replace("%value%", String.valueOf(var.isSheared())));
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = new ItemBuilder(Material.SHEARS);
        item.withName("&6Sheared: &e%value%");
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntitySheepPet) {
            IEntitySheepPet pet = (IEntitySheepPet) entityPet;
            if (pet.isSheared()) {
                pet.setSheared(false);
            } else {
                pet.setSheared(true);
            }
        }
    }
}
