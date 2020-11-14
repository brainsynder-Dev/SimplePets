package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityPiglinPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Dancing extends MenuItemAbstract {
    public Dancing(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Dancing(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("dancing", 0);
        if (item != null) {
            if (entityPet instanceof IEntityPiglinPet) {
                IEntityPiglinPet var = (IEntityPiglinPet) entityPet;
                item.withName(item.getName().replace("%value%", String.valueOf(var.isDancing())));
            }
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.PLAYER_HEAD).setTexture("http://textures.minecraft.net/texture/9f18107d275f1cb3a9f973e5928d5879fa40328ff3258054db6dd3e7c0ca6330");
        item.withName("&6Dancing: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityPiglinPet) {
            IEntityPiglinPet pet = (IEntityPiglinPet) entityPet;
            pet.setDancing(!pet.isDancing());
        }
    }
}
