package simplepets.brainsynder.api.pet.data.fox;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityFoxPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.FoxType;

@Namespace(namespace = "type")
public class FoxTypeData extends PetData<IEntityFoxPet> {
    public FoxTypeData() {
        addDefaultItem(FoxType.RED.name(), new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &aRed")
                .setTexture("http://textures.minecraft.net/texture/d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a"));
        addDefaultItem(FoxType.WHITE.name(), new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &aWhite")
                .setTexture("http://textures.minecraft.net/texture/ddcd0db8cbe8f1e0ab1ec0a9385fb9288da84d3202c1c397da76ee1035e608b0"));
    }

    @Override
    public Object getDefaultValue() {
        return FoxType.RED;
    }

    @Override
    public void onLeftClick(IEntityFoxPet entity) {
        entity.setFoxType(FoxType.getNext(entity.getFoxType()));
    }

    @Override
    public Object value(IEntityFoxPet entity) {
        return entity.getFoxType();
    }
}
