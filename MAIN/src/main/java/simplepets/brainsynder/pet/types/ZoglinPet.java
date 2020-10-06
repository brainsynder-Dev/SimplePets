package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityZoglinPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.AdditionalData;
import simplepets.brainsynder.wrapper.EntityWrapper;

@AdditionalData(passive = false)
@SupportedVersion(version = ServerVersion.v1_16_R1)
public class ZoglinPet extends PetType {
    public ZoglinPet(PetCore plugin) {
        super(plugin, "zoglin", SoundMaker.ENTITY_HOGLIN_AMBIENT, EntityWrapper.ZOGLIN);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/e67e18602e03035ad68967ce090235d8996663fb9ea47578d3a7ebbc42a5ccf9")
                .withName("&f&lZoglin Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityZoglinPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.AGE;
    }
}
