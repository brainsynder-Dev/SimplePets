package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityAxolotlPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.AxolotlVariant;

@Namespace(namespace = "playing_dead")
public class AxolotlPlayDeadData extends PetData<IEntityAxolotlPet> {
    public AxolotlPlayDeadData() {
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &cfalse")
                .setTexture(AxolotlVariant.LUCY.getTexture()));
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/88fd654d856bde8b69f0c3567d28fafe94e71eae10d32ea59ee23e9bd64b41b0"));

    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityAxolotlPet entity) {
        entity.setPlayingDead(!entity.isPlayingDead());
    }

    @Override
    public Object value(IEntityAxolotlPet entity) {
        return entity.isPlayingDead();
    }
}
