package simplepets.brainsynder.menu.menuItems;

import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityCreeperPet;
import simplepets.brainsynder.api.entity.hostile.IEntityVexPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Powered extends MenuItemAbstract {

    public Powered(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Powered(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("powered", 0);
        if (entityPet instanceof IEntityCreeperPet) {
            IEntityCreeperPet var = (IEntityCreeperPet) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isPowered())));
        }
        if (entityPet instanceof IEntityVexPet) {
            IEntityVexPet var = (IEntityVexPet) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isPowered())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Utilities.fetchMaterial("SULPHUR", "GUNPOWDER"));
        item.withName("&6Powered: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityVexPet) {
            IEntityVexPet bat = (IEntityVexPet) entityPet;
            if (bat.isPowered()) {
                bat.setPowered(false);
            } else {
                bat.setPowered(true);
            }
        }
        if (entityPet instanceof IEntityCreeperPet) {
            IEntityCreeperPet bat = (IEntityCreeperPet) entityPet;
            if (bat.isPowered()) {
                bat.setPowered(false);
            } else {
                bat.setPowered(true);
            }
        }
    }
}
