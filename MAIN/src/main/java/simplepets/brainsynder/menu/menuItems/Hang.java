package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityBatPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Hang extends MenuItemAbstract {


    public Hang(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Hang(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("hang", 0);
        if (entityPet instanceof IEntityBatPet) {
            IEntityBatPet var = (IEntityBatPet) entityPet;
            item.withName(item.getName().replace("%value%", PetCore.get().getMessages().getTrueOrFalse(var.isHanging())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.FEATHER);
        item.withName("&6Hanging: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityBatPet) {
            IEntityBatPet bat = (IEntityBatPet) entityPet;
            bat.setHanging(!bat.isHanging());
        }
    }
}
