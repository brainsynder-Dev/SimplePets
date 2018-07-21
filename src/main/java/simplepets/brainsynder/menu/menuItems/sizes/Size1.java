package simplepets.brainsynder.menu.menuItems.sizes;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ISizable;
import simplepets.brainsynder.api.entity.hostile.IEntityPhantomPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Size1 extends MenuItemAbstract {

    public Size1(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Size1(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        return type.getDataItemByName("size1", 0);
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        return new ArrayList<>(Collections.singleton(new ItemBuilder(Material.SLIME_BLOCK).setTexture("&6&lSize: &e1")));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityPhantomPet) {
            IEntityPhantomPet slime = (IEntityPhantomPet) entityPet;
            slime.setSize(1);
        }
        if (entityPet instanceof ISizable) {
            ISizable slime = (ISizable) entityPet;
            slime.setSize(1);
        }
    }
}
