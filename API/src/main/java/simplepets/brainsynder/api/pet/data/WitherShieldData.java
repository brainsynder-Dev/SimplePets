package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.hostile.IEntityWitherPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "shielded")
public class WitherShieldData extends PetData<IEntityWitherPet> {
    public WitherShieldData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/a435164c05cea299a3f016bbbed05706ebb720dac912ce4351c2296626aecd9a"));
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &cfalse")
                .setTexture("http://textures.minecraft.net/texture/ee280cefe946911ea90e87ded1b3e18330c63a23af5129dfcfe9a8e166588041"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityWitherPet entity) {
        entity.setShielded(!entity.isShielded());
    }

    @Override
    public Object value(IEntityWitherPet entity) {
        return entity.isShielded();
    }
}
