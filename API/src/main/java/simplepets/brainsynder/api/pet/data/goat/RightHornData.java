package simplepets.brainsynder.api.pet.data.goat;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityGoatPet;
import simplepets.brainsynder.api.pet.PetData;

@SupportedVersion(version = ServerVersion.v1_19)
@Namespace(namespace = "right-horn")
public class RightHornData extends PetData<IEntityGoatPet> {
    public RightHornData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/957607099d06b7a8b1327093cd0a488be7c9f50b6121b22151271b59170f3c21"));
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &cfalse")
                .setTexture("http://textures.minecraft.net/texture/957607099d06b7a8b1327093cd0a488be7c9f50b6121b22151271b59170f3c21"));
    }

    @Override
    public Object getDefaultValue() {
        return true;
    }

    @Override
    public void onLeftClick(IEntityGoatPet entity) {
        entity.setRightHorn(!entity.hasRightHorn());
    }

    @Override
    public Object value(IEntityGoatPet entity) {
        return entity.hasRightHorn();
    }
}
