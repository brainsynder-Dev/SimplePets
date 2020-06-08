package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.item.ItemBuilder;
import org.apache.commons.lang.WordUtils;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityMooshroomPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;
import simplepets.brainsynder.wrapper.MooshroomType;

import java.util.ArrayList;
import java.util.List;

@ValueType(def = "RED", target = "https://github.com/brainsynder-Dev/SimplePets/blob/master/MAIN/src/main/java/simplepets/brainsynder/wrapper/MooshroomType.java")
public class MooshroomColor extends MenuItemAbstract<IEntityMooshroomPet> {

    public MooshroomColor(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public MooshroomColor(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        return type.getDataItemByName(getTargetName(), entityPet.getMooshroomType().ordinal());
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        for (MooshroomType type : MooshroomType.values()) {
            ItemBuilder builder = type.getIcon();
            builder.withName("&6Color: &e"+ WordUtils.capitalize(type.name().toLowerCase()));
            items.add(builder);
        }
        return items;
    }

    @Override
    public void onLeftClick() {
        entityPet.setMooshroomType(MooshroomType.getNext(entityPet.getMooshroomType()));
    }

    @Override
    public void onRightClick() {
        entityPet.setMooshroomType(MooshroomType.getPrevious(entityPet.getMooshroomType()));
    }

    @Override
    public String getTargetName() {
        return "type";
    }

    @Override
    public boolean isSupported() {
        return ServerVersion.isEqualNew(ServerVersion.v1_14_R1);
    }
}
