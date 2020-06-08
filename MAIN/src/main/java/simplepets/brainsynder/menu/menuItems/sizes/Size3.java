package simplepets.brainsynder.menu.menuItems.sizes;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.ISizable;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;

import java.util.ArrayList;
import java.util.List;

public class Size3 extends MenuItemAbstract {

    public Size3(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Size3(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        return type.getDataItemByName("size3", 0);
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        items.add(new ItemBuilder(Material.SLIME_BLOCK).withName("&6&lSize: &e3"));
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof ISizable) {
            ISizable slime = (ISizable) entityPet;
            slime.setSize(3);
        }
    }
}
