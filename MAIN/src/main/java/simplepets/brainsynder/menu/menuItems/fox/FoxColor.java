package simplepets.brainsynder.menu.menuItems.fox;

import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityFoxPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.FoxType;

import java.util.ArrayList;
import java.util.List;

public class FoxColor extends MenuItemAbstract {

    public FoxColor(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public FoxColor(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName(getTargetName(), 0);
        if (entityPet instanceof IEntityFoxPet) {
            IEntityFoxPet var = (IEntityFoxPet) entityPet;
            item = type.getDataItemByName(getTargetName(), var.getFoxType().ordinal());
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        ItemBuilder builder = ItemBuilder.getSkull(simple.brainsynder.utils.SkullType.PLAYER);
        items.add(builder.clone().withName("&6Type: &eRed")
                .setTexture("http://textures.minecraft.net/texture/d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a"));
        items.add(builder.clone().withName("&6Type: &eWhite")
                .setTexture("http://textures.minecraft.net/texture/ddcd0db8cbe8f1e0ab1ec0a9385fb9288da84d3202c1c397da76ee1035e608b0"));
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityFoxPet) {
            IEntityFoxPet var = (IEntityFoxPet) entityPet;
            var.setFoxType(FoxType.getNext(var.getFoxType()));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IEntityFoxPet) {
            IEntityFoxPet var = (IEntityFoxPet) entityPet;
            var.setFoxType(FoxType.getPrevious(var.getFoxType()));
        }
    }

    @Override
    public String getTargetName() {
        return "type";
    }
}
