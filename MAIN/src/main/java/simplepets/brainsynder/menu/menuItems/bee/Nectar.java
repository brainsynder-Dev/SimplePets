package simplepets.brainsynder.menu.menuItems.bee;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityBeePet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SupportedVersion(version = ServerVersion.v1_15_R1)
@ValueType(type = "boolean", def = "false")
public class Nectar extends MenuItemAbstract<IEntityBeePet> {
    public Nectar(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Nectar(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("nectar", 0);
        if (item != null) item.withName(item.getName().replace("%value%", String.valueOf(entityPet.hasNectar())));
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder (org.bukkit.Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/b727d0ab03f5cd022f8705d3f7f133ca4920eae8e1e47b5074433a137e691e4e");
        item.withName("&6Has Nectar: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        entityPet.setHasNectar(!entityPet.hasNectar());
    }
}
