package simplepets.brainsynder.api.pet.data.panda;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityPandaPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "sneeze")
public class PandaSneezeData extends PetData<IEntityPandaPet> {
    public PandaSneezeData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/5c2d25e956337d82791fa0e6617a40086f02d6ebfbfd5a6459889cf206fca787"));
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &cfalse")
                .setTexture("http://textures.minecraft.net/texture/dca096eea506301bea6d4b17ee1605625a6f5082c71f74a639cc940439f47166"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityPandaPet entity) {
        entity.setSneezing(!entity.isSneezing());
    }

    @Override
    public Object value(IEntityPandaPet entity) {
        return entity.isSneezing();
    }
}
