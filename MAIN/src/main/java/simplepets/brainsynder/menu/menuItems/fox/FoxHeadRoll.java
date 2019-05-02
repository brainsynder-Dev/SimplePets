package simplepets.brainsynder.menu.menuItems.fox;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityFoxPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

import java.util.ArrayList;
import java.util.List;

public class FoxHeadRoll extends MenuItemAbstract<IEntityFoxPet> {

    public FoxHeadRoll(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public FoxHeadRoll(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder builder = type.getDataItemByName(getTargetName(), 0);
        builder.withName(formatName(builder, (entity, name) -> {
            name = name.replace("%value%", String.valueOf(entity.isRollingHead()));
            return name;
        }));
        return builder;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        items.add(ItemBuilder.getSkull(simple.brainsynder.utils.SkullType.PLAYER)
                .setTexture("http://textures.minecraft.net/texture/d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a")
                .withName("&6&lHead Roll: &e%value%"));
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityFoxPet) {
            IEntityFoxPet var = entityPet;
            var.setRollingHead(!var.isRollingHead());
        }
    }

    @Override
    public boolean isSupported() {
        return ServerVersion.isEqualNew(ServerVersion.v1_14_R1);
    }

    @Override
    public String getTargetName() {
        return "headRoll";
    }
}
