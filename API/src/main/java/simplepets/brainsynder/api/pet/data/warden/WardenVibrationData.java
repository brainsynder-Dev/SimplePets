package simplepets.brainsynder.api.pet.data.warden;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.hostile.IEntityWardenPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "vibration")
public class WardenVibrationData extends PetData<IEntityWardenPet> {
    public WardenVibrationData() {
        addDefaultItem("true", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &atrue")
                .setTexture("http://textures.minecraft.net/texture/c6f74361fb00490a0a98eeb814544ecdd775cb55633dbb114e60d27004cb1020"));
        addDefaultItem("false", new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &cfalse")
                .setTexture("http://textures.minecraft.net/texture/1dfd13ca08bf973bfef0293d770704a11ef5a9fe20d40671fb066724d3e18d8"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityWardenPet entity) {
        entity.setVibrationEffect(!entity.getVibrationEffect());
    }

    @Override
    public Object value(IEntityWardenPet entity) {
        return entity.getVibrationEffect();
    }
}
