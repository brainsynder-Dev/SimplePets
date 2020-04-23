package simplepets.brainsynder.menu.menuItems.sizes;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.ISizable;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

import java.util.ArrayList;
import java.util.List;

public class Size4 extends MenuItemAbstract {

    public Size4(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Size4(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        return type.getDataItemByName("size4", 0);
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        items.add(new ItemBuilder(Material.SLIME_BLOCK).withName("&6&lSize: &e4"));
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof ISizable) {
            ISizable slime = (ISizable) entityPet;
            slime.setSize(4);
        }
    }
}
