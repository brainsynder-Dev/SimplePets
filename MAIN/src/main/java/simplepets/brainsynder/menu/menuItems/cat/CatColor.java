package simplepets.brainsynder.menu.menuItems.cat;

import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityCatPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ValueType;
import simplepets.brainsynder.wrapper.CatType;

import java.util.ArrayList;
import java.util.List;

@ValueType(def = "WHITE", target = "https://github.com/brainsynder-Dev/SimplePets/blob/master/MAIN/src/main/java/simplepets/brainsynder/wrapper/CatType.java")
public class CatColor extends MenuItemAbstract<IEntityCatPet> {

    public CatColor(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public CatColor(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        return type.getDataItemByName(getTargetName(), entityPet.getCatType().ordinal());
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        for (CatType type : CatType.values()) items.add(type.getIcon());
        return items;
    }

    @Override
    public void onLeftClick() {
        entityPet.setCatType(CatType.getNext(entityPet.getCatType()));
    }

    @Override
    public void onRightClick() {
        entityPet.setCatType(CatType.getPrevious(entityPet.getCatType()));
    }

    @Override
    public String getTargetName() {
        return "type";
    }
}
