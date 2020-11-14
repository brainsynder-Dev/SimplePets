package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IChestedAbstractPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Chested extends MenuItemAbstract {
    public Chested(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Chested(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("chested", 0);
        if (item != null) {
            if (entityPet instanceof IChestedAbstractPet) {
                IChestedAbstractPet var = (IChestedAbstractPet) entityPet;
                item.withName(item.getName().replace("%value%", PetCore.get().getMessages().getTrueOrFalse(var.isChested())));
            }
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.CHEST);
        item.withName("&6Chested: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IChestedAbstractPet) {
            IChestedAbstractPet chested = (IChestedAbstractPet) entityPet;
            chested.setChested(!chested.isChested());
        }
    }
}
