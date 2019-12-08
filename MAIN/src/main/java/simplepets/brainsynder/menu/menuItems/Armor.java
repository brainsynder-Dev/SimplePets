package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.player.PetOwner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Armor extends MenuItemAbstract<IEntityArmorStandPet> {
    public Armor(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Armor(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        return type.getDataItemByName("armor", 0);
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.IRON_CHESTPLATE);
        item.withName("&6Armor Customization");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (!entityPet.isOwner()) return;
        PetCore.get().getInvLoaders().ARMOR.open(PetOwner.getPetOwner(entityPet.getOwner()));
    }
}
