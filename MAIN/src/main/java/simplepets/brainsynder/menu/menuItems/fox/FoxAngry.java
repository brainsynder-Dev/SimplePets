package simplepets.brainsynder.menu.menuItems.fox;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityFoxPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

import java.util.ArrayList;
import java.util.List;

public class FoxAngry extends MenuItemAbstract<IEntityFoxPet> {

    public FoxAngry(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public FoxAngry(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder builder = type.getDataItemByName(getTargetName(), 0);
        builder.withName(formatName(builder, (entity, name) -> {
            name = name.replace("%value%", String.valueOf(entity.isAggressive()));
            return name;
        }));
        return builder;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        ItemBuilder item = ItemBuilder.getColored(simple.brainsynder.utils.MatType.WOOL, 14);
        item.withName("&6Angry: &e%value%");
        items.add(item);
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityFoxPet) {
            IEntityFoxPet var = entityPet;
            var.setAggressive(!var.isAggressive());
        }
    }

    @Override
    public boolean isSupported() {
        return ServerVersion.isEqualNew(ServerVersion.v1_14_R1);
    }

    @Override
    public String getTargetName() {
        return "angry";
    }
}
