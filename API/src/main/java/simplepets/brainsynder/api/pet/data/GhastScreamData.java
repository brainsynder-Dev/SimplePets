package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.hostile.IEntityGhastPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "screaming")
public class GhastScreamData extends PetData<IEntityGhastPet> {
    public GhastScreamData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/78f77eeeef6ffb2f6818e57698794ae0351ab32ba234d621c22fe4ce8e1599d2"));
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &cfalse")
                .setTexture("http://textures.minecraft.net/texture/7a8b714d32d7f6cf8b37e221b758b9c599ff76667c7cd45bbc49c5ef19858646"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityGhastPet entity) {
        entity.setScreaming(!entity.isScreaming());
    }

    @Override
    public Object value(IEntityGhastPet entity) {
        return entity.isScreaming();
    }
}
