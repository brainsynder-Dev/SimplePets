package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityWitchPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Potion extends MenuItemAbstract {
    public Potion(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Potion(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("potion", 0);
        if (entityPet instanceof IEntityWitchPet) {
            IEntityWitchPet var = (IEntityWitchPet) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isDrinkingPotion())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.POTION);
        item.withName("&6Drinking Potion: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityWitchPet) {
            IEntityWitchPet pet = (IEntityWitchPet) entityPet;
            pet.setDrinkingPotion(!pet.isDrinkingPotion());
        }
    }
}
