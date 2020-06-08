package simplepets.brainsynder.menu.menuItems.cat;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityCatPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

import java.util.ArrayList;
import java.util.List;

@SupportedVersion(version = ServerVersion.v1_14_R1)
@ValueType(def = "WHITE", target = "https://github.com/brainsynder-Dev/SimplePets/blob/master/MAIN/src/main/java/simplepets/brainsynder/wrapper/DyeColorWrapper.java")
public class CatCollar extends MenuItemAbstract<IEntityCatPet> {

    public CatCollar(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public CatCollar(PetType type) {
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
