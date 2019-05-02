package simplepets.brainsynder.menu.menuItems.panda;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityPandaPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

public class PandaSit extends MenuItemAbstract<IEntityPandaPet> {

    public PandaSit(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public PandaSit(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder builder = type.getDataItemByName(getTargetName(), 0);
        builder.withName(formatName(builder, (entity, name) -> {
            name = name.replace("%value%", String.valueOf(entity.isScared()));
            return name;
        }));
        return builder;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        items.add(new ItemBuilder(Utilities.fetchMaterial("WOOD_STAIRS", "OAK_STAIRS")).withName("&6&lSitting: &e%value%"));
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityPandaPet) {
            IEntityPandaPet panda = entityPet;
            panda.setScared(!panda.isScared());
        }
    }

    @Override
    public boolean isSupported() {
        return ServerVersion.isEqualNew(ServerVersion.v1_14_R1);
    }

    @Override
    public String getTargetName() {
        return "sitting";
    }
}
