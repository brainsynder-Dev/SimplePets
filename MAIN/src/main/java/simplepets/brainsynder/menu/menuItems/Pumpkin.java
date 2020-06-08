package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntitySnowmanPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Pumpkin extends MenuItemAbstract {

    public Pumpkin(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Pumpkin(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("pumpkin", 0);
        if (entityPet instanceof IEntitySnowmanPet) {
            IEntitySnowmanPet var = (IEntitySnowmanPet) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.hasPumpkin())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.PUMPKIN);
        item.withName("&6Pumpkin: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntitySnowmanPet) {
            IEntitySnowmanPet var = (IEntitySnowmanPet) entityPet;
            var.setHasPumpkin(!var.hasPumpkin());
        }
    }
}
