package simplepets.brainsynder.api.pet.data.bee;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityBeePet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "nectar")
public class NectarData extends PetData<IEntityBeePet> {
    public NectarData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/b727d0ab03f5cd022f8705d3f7f133ca4920eae8e1e47b5074433a137e691e4e"));
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &cfalse")
                .setTexture("http://textures.minecraft.net/texture/fa2cb74c13245d3ce9bacc8b1600af02fd7c91f501feaf97364e1f8b6f04f47f"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityBeePet entity) {
        entity.setHasNectar(!entity.hasNectar());
    }

    @Override
    public Object value(IEntityBeePet entity) {
        return entity.hasNectar();
    }
}
