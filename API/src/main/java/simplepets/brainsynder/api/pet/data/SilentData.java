package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "silent")
public class SilentData extends PetData {
    public SilentData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/b1f327c3f349158d209b4867d68ffb1890bc57a01ba9483e3fffe4ec7fdea0b0"));
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &cfalse")
                .setTexture("http://textures.minecraft.net/texture/5461518b74d5f7016f72294756fc68c5471110cc97f3bb093e0c6ed94a9e3"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityPet entity) {
        if (entity instanceof IEntityControllerPet controller) {
            controller.getVisibleEntity().setPetSilent(!controller.getVisibleEntity().isPetSilent());
            return;
        }
        entity.setPetSilent(!entity.isPetSilent());
    }

    @Override
    public Object value(IEntityPet entity) {
        if (entity instanceof IEntityControllerPet controller) return controller.getVisibleEntity().isPetSilent();
        return entity.isPetSilent();
    }
}
