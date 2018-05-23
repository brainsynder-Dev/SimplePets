package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityGhastPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GhastScream extends MenuItemAbstract {


    public GhastScream(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public GhastScream(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("ghastscream", 0);
        if (entityPet instanceof IEntityGhastPet) {
            IEntityGhastPet var = (IEntityGhastPet) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isScreaming())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.PLAYER_HEAD);
        item.withName("&6Screaming: &e%value%");
        item.setTexture("http://textures.minecraft.net/texture/8b6a72138d69fbbd2fea3fa251cabd87152e4f1c97e5f986bf685571db3cc0");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityGhastPet) {
            IEntityGhastPet pet = (IEntityGhastPet) entityPet;
            if (pet.isScreaming()) {
                pet.setScreaming(false);
            } else {
                pet.setScreaming(true);
            }
        }
    }
}
