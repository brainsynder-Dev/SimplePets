package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityVindicatorPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Johnny extends MenuItemAbstract {

    public Johnny(PetType type) {
        super(type);
    }

    public Johnny(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("johnny", 0);
        if (entityPet instanceof IEntityVindicatorPet) {
            IEntityVindicatorPet var = (IEntityVindicatorPet) entityPet;
            item.withName(item.getName().replace("%value%", PetCore.get().getMessages().getTrueOrFalse(var.isJohnny())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.IRON_AXE);
        item.withName("&6Johnny: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityVindicatorPet) {
            IEntityVindicatorPet pet = (IEntityVindicatorPet) entityPet;
            pet.setJohnny(!pet.isJohnny());
        }
    }
}
