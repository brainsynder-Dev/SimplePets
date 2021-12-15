package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "burning")
public class BurningData extends PetData<IEntityPet> {
    public BurningData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/4080bbefca87dc0f36536b6508425cfc4b95ba6e8f5e6a46ff9e9cb488a9ed"));
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &cfalse")
                .setTexture("http://textures.minecraft.net/texture/4080bbefca87dc0f36536b6508425cfc4b95ba6e8f5e6a46ff9e9cb488a9ed"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityPet entity) {
        if (entity instanceof IEntityControllerPet controller) {
            controller.getVisibleEntity().setBurning(!controller.getVisibleEntity().isBurning());
            return;
        }

        entity.setBurning(!entity.isBurning());
    }

    @Override
    public Object value(IEntityPet entity) {
        if (entity instanceof IEntityControllerPet controller) return controller.getVisibleEntity().isBurning();
        return entity.isBurning();
    }
}
