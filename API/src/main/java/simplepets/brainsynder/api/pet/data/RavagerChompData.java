package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.hostile.IEntityRavagerPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "chomping")
public class RavagerChompData extends PetData<IEntityRavagerPet> {
    public RavagerChompData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/cd20bf52ec390a0799299184fc678bf84cf732bb1bd78fd1c4b441858f0235a8")
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/cd20bf52ec390a0799299184fc678bf84cf732bb1bd78fd1c4b441858f0235a8")
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityRavagerPet entity) {
        entity.setChomping(!entity.isChomping());
    }

    @Override
    public Object value(IEntityRavagerPet entity) {
        return entity.isChomping();
    }
}
