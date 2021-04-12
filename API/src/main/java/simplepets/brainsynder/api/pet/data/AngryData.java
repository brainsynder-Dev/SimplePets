package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityWolfPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "angry")
public class AngryData extends PetData<IEntityWolfPet> {
    public AngryData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/8d1aa7e3b9564b3846f1dea14f1b1ccbf399bbb23b952dbd7eec41802a289c96"));
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &cfalse")
                .setTexture("http://textures.minecraft.net/texture/69d1d3113ec43ac2961dd59f28175fb4718873c6c448dfca8722317d67"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityWolfPet entity) {
        entity.setAngry(!entity.isAngry());
    }

    @Override
    public Object value(IEntityWolfPet entity) {
        return entity.isAngry();
    }
}
