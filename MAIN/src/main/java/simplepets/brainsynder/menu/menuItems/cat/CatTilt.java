package simplepets.brainsynder.menu.menuItems.cat;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.utils.SkullType;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityCatPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

import java.util.ArrayList;
import java.util.List;

public class CatTilt extends MenuItemAbstract<IEntityCatPet> {

    public CatTilt(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public CatTilt(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder builder = type.getDataItemByName(getTargetName(), 0);
        builder.withName(formatName(builder, (entity, name) -> {
            name = name.replace("%value%", String.valueOf(entity.isHeadUp()));
            return name;
        }));
        return builder;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        items.add(ItemBuilder.getSkull(SkullType.PLAYER).withName("&6Head Up: &e%value%"));
        return items;
    }

    @Override
    public void onLeftClick() {
        entityPet.setHeadUp(!entityPet.isHeadUp());
    }

    @Override
    public String getTargetName() {
        return "head_up";
    }
}
