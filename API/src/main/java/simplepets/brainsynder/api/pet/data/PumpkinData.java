package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntitySnowmanPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "pumpkin")
public class PumpkinData extends PetData<IEntitySnowmanPet> {
    public PumpkinData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/4224b25796529ef58a36da6f227dd3ef40a842172d91f396aabeed7e04dbd5b1"));
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &cfalse")
                .setTexture("http://textures.minecraft.net/texture/11136616d8c4a87a54ce78a97b551610c2b2c8f6d410bc38b858f974b113b208"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntitySnowmanPet entity) {
        entity.setHasPumpkin(!entity.hasPumpkin());
    }

    @Override
    public Object value(IEntitySnowmanPet entity) {
        return entity.hasPumpkin();
    }
}
