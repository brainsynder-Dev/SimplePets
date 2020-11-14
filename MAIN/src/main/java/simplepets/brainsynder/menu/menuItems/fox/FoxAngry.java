package simplepets.brainsynder.menu.menuItems.fox;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityFoxPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.List;

@SupportedVersion(version = ServerVersion.v1_14_R1)
@ValueType(type = "boolean", def = "false")
public class FoxAngry extends MenuItemAbstract<IEntityFoxPet> {

    public FoxAngry(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public FoxAngry(PetType type) {
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
        ItemBuilder item = lib.brainsynder.nms.DataConverter.getColoredMaterial(lib.brainsynder.nms.DataConverter.MaterialType.WOOL, 14);
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
