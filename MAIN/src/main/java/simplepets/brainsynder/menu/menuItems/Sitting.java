package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.ITameable;
import simplepets.brainsynder.api.entity.passive.IEntityOcelotPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Sitting extends MenuItemAbstract {
    public Sitting(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Sitting(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("sitting", 0);
        if (entityPet instanceof ITameable) {
            ITameable var = (ITameable) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isSitting())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.OAK_STAIRS);
        item.withName("&6Sitting: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof ITameable) {
            ITameable var = (ITameable) entityPet;
            var.setSitting(!var.isSitting());
        }
    }

    @Override
    public boolean isSupported() {
        if ((entityPet instanceof IEntityOcelotPet) && ServerVersion.isEqualNew(ServerVersion.v1_14_R1))
            return false;
        return super.isSupported();
    }
}
