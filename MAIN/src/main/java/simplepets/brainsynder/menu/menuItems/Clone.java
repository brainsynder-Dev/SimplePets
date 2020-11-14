package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Clone extends MenuItemAbstract {

    public Clone(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Clone(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("clone", 0);
        if (item != null) {
            if (entityPet instanceof IEntityArmorStandPet) {
                IEntityArmorStandPet var = (IEntityArmorStandPet) entityPet;
                item.withName(item.getName().replace("%value%", PetCore.get().getMessages().getTrueOrFalse(var.isOwner())));
            }
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.PLAYER_HEAD);
        item.withName("&6IsClone: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityArmorStandPet) {
            IEntityArmorStandPet bat = (IEntityArmorStandPet) entityPet;
            bat.setOwner(!bat.isOwner());
        }
    }
}
