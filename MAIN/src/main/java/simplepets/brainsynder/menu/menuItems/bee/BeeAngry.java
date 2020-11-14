package simplepets.brainsynder.menu.menuItems.bee;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import simplepets.brainsynder.PetCore;
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
public class BeeAngry extends MenuItemAbstract<IEntityBeePet> {
    public BeeAngry(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public BeeAngry(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName(getTargetName(), 0);
        if (item != null) item.withName(item.getName().replace("%value%", PetCore.get().getMessages().getTrueOrFalse(entityPet.isAngry())));
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder (org.bukkit.Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/e400223f1fa54741d421d7e8046409d5f3e15c7f4364b1b739940208f3b686d4");
        item.withName("&6Angry: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        entityPet.setAngry(!entityPet.isAngry());
    }

    @Override
    public String getTargetName() {
        return "angry";
    }
}
