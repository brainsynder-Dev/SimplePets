package simplepets.brainsynder.menu.menuItems.cat;

import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityCatPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

import java.util.ArrayList;
import java.util.List;

public class CatCollar extends MenuItemAbstract<IEntityCatPet> {

    public CatCollar(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public CatCollar(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        return type.getDataItemByName(getTargetName(), entityPet.getCollarColor().ordinal());
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        for (DyeColorWrapper color : DyeColorWrapper.values()) items.add(color.getIcon());
        return items;
    }

    @Override
    public void onLeftClick() {
        entityPet.setCollarColor(DyeColorWrapper.getNext(entityPet.getCollarColor()));
    }

    @Override
    public void onRightClick() {
        entityPet.setCollarColor(DyeColorWrapper.getPrevious(entityPet.getCollarColor()));
    }

    @Override
    public String getTargetName() {
        return "collar";
    }
}
