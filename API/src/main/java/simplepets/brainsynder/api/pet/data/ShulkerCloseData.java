package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.hostile.IEntityShulkerPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "closed")
public class ShulkerCloseData extends PetData<IEntityShulkerPet> {
    public ShulkerCloseData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/25c4d24affdd48102620361527d2156e18c223bae5189ac439815643f3cff9d"));
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &cfalse")
                .setTexture("http://textures.minecraft.net/texture/1433a4b73273a64c8ab2830b0fff777a61a488c92f60f83bfb3e421f428a44"));
    }

    @Override
    public Object getDefaultValue() {
        return true;
    }

    @Override
    public void onLeftClick(IEntityShulkerPet entity) {
        entity.setShulkerClosed(!entity.isShulkerClosed());
    }

    @Override
    public Object value(IEntityShulkerPet entity) {
        return entity.isShulkerClosed();
    }
}
