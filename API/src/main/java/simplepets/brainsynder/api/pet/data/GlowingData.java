package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityGlowSquidPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "glowing")
public class GlowingData extends PetData<IEntityGlowSquidPet> {
    public GlowingData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/3e94a1bb1cb00aaa153a74daf4b0eea20b8974522fe9901eb55aef478ebeff0d"));
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &cfalse")
                .setTexture("http://textures.minecraft.net/texture/b4f546cd512da37c0282fe531e7cdb3c5dc35baa32696099e68d3b894c16d2c"));
    }

    @Override
    public Object getDefaultValue() {
        return true;
    }

    @Override
    public void onLeftClick(IEntityGlowSquidPet entity) {
        entity.setSquidGlowing(!entity.isSquidGlowing());
    }

    @Override
    public Object value(IEntityGlowSquidPet entity) {
        return entity.isSquidGlowing();
    }
}
