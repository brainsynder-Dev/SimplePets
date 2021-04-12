package simplepets.brainsynder.api.pet.data.bee;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityBeePet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "angry")
public class BeeAngryData extends PetData<IEntityBeePet> {
    public BeeAngryData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/e400223f1fa54741d421d7e8046409d5f3e15c7f4364b1b739940208f3b686d4"));
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
        entity.setAngry(!entity.isAngry());
    }

    @Override
    public Object value(IEntityBeePet entity) {
        return entity.isAngry();
    }
}
