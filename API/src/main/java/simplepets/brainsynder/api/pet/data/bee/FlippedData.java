package simplepets.brainsynder.api.pet.data.bee;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityBeePet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "flipped")
public class FlippedData extends PetData<IEntityBeePet> {
    public FlippedData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/fa2cb74c13245d3ce9bacc8b1600af02fd7c91f501feaf97364e1f8b6f04f47f"));
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
        entity.setFlipped(!entity.isFlipped());
    }

    @Override
    public Object value(IEntityBeePet entity) {
        return entity.isFlipped();
    }
}
