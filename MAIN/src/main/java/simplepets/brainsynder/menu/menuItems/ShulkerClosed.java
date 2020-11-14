package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityShulkerPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.storage.files.Messages;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "true")
public class ShulkerClosed extends MenuItemAbstract {

    public ShulkerClosed(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public ShulkerClosed(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("shulkerclosed", 0);
        if (getEntityPet() instanceof IEntityShulkerPet) {
            IEntityShulkerPet var = (IEntityShulkerPet) getEntityPet();
            item.withName(item.getName().replace("%value%", Messages.getTrueOrFalse(var.isClosed())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.SHULKER_SHELL);
        item.withName("&6Closed: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityShulkerPet) {
            IEntityShulkerPet var = (IEntityShulkerPet) entityPet;
            var.setClosed(!var.isClosed());
        }
    }
}
