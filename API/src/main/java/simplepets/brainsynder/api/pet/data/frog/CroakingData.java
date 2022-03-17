package simplepets.brainsynder.api.pet.data.frog;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityFrogPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "croaking")
public class CroakingData extends PetData<IEntityFrogPet> {
    public CroakingData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/1b20e0c01e2a241fb6fbf45045b9c9dbfecf745c62a8fda6eb6522fc2d53e2cf"));
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &cfalse")
                .setTexture("http://textures.minecraft.net/texture/1b20e0c01e2a241fb6fbf45045b9c9dbfecf745c62a8fda6eb6522fc2d53e2cf"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityFrogPet entity) {
        entity.setCroaking(!entity.isCroaking());
    }

    @Override
    public Object value(IEntityFrogPet entity) {
        return entity.isCroaking();
    }
}
