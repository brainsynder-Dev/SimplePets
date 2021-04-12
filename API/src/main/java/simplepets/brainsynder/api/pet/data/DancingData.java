package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.hostile.IEntityPiglinPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "dancing")
public class DancingData extends PetData<IEntityPiglinPet> {
    public DancingData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/9f18107d275f1cb3a9f973e5928d5879fa40328ff3258054db6dd3e7c0ca6330"));
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &cfalse")
                .setTexture("http://textures.minecraft.net/texture/9f18107d275f1cb3a9f973e5928d5879fa40328ff3258054db6dd3e7c0ca6330"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityPiglinPet entity) {
        entity.setDancing(!entity.isDancing());
    }

    @Override
    public Object value(IEntityPiglinPet entity) {
        return entity.isDancing();
    }
}
