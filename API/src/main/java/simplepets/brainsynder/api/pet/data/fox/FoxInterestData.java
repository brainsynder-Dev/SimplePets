package simplepets.brainsynder.api.pet.data.fox;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityFoxPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "interested")
public class FoxInterestData extends PetData<IEntityFoxPet> {
    public FoxInterestData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a"));
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &cfalse")
                .setTexture("http://textures.minecraft.net/texture/d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityFoxPet entity) {
        entity.setInterested(!entity.isInterested());
    }

    @Override
    public Object value(IEntityFoxPet entity) {
        return entity.isInterested();
    }
}
