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

public class PandaPlay extends MenuItemAbstract<IEntityPandaPet> {

    public PandaPlay(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public PandaPlay(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder builder = type.getDataItemByName(getTargetName(), 0);
        builder.withName(formatName(builder, (entity, name) -> {
            name = name.replace("%value%", String.valueOf(entity.isPlaying()));
            return name;
        }));
        return builder;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        items.add(new ItemBuilder(Utilities.fetchMaterial("BAMBOO", "APPLE")).withName("&6&lPlaying: &e%value%"));
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityPandaPet) {
            IEntityPandaPet panda = entityPet;
            panda.setPlaying(!panda.isPlaying());
        }
    }

    @Override
    public boolean isSupported() {
        return ServerVersion.isEqualNew(ServerVersion.v1_14_R1);
    }

    @Override
    public String getTargetName() {
        return "playing";
    }
}
