package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Silent extends MenuItemAbstract {
    public Silent(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Silent(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("silent", (entityPet.isPetSilent() ? 1 : 0));

        if (item != null) {
            item.withName(item.getName().replace("%value%", String.valueOf(entityPet.isPetSilent())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        ItemBuilder item = new ItemBuilder(Material.PLAYER_HEAD);

        // Has Sound
        item.setTexture("http://textures.minecraft.net/texture/5461518b74d5f7016f72294756fc68c5471110cc97f3bb093e0c6ed94a9e3");
        items.add(item.clone());

        // Sound Muted
        item.setTexture("http://textures.minecraft.net/texture/b1f327c3f349158d209b4867d68ffb1890bc57a01ba9483e3fffe4ec7fdea0b0");
        items.add(item.clone());

        item.withName("&6Silent: &e%value%");
        return items;
    }

    @Override
    public void onLeftClick() {
        entityPet.setPetSilent(!entityPet.isPetSilent());
    }
}
