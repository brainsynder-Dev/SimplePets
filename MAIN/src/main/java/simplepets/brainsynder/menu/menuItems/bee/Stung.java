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
public class Stung extends MenuItemAbstract<IEntityBeePet> {
    public Stung(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Stung(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("stung", 0);
        if (item != null) item.withName(item.getName().replace("%value%", String.valueOf(!entityPet.hasStung())));

        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder (org.bukkit.Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/fa2cb74c13245d3ce9bacc8b1600af02fd7c91f501feaf97364e1f8b6f04f47f");
        item.withName("&6Has Stinger: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        entityPet.setHasStung(!entityPet.hasStung());
    }
}
