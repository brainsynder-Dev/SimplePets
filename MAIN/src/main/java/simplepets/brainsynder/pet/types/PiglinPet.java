package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityPiglinPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.AdditionalData;
import simplepets.brainsynder.wrapper.EntityWrapper;

@AdditionalData(passive = false)
@SupportedVersion(version = ServerVersion.v1_16_R1)
public class PiglinPet extends PetType {
    public PiglinPet(PetCore plugin) {
        super(plugin, "piglin", SoundMaker.ENTITY_PIGLIN_AMBIENT, EntityWrapper.PIGLIN);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/9f18107d275f1cb3a9f973e5928d5879fa40328ff3258054db6dd3e7c0ca6330")
                .withName("&f&lPiglin Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityPiglinPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.PIGLIN;
    }
}
