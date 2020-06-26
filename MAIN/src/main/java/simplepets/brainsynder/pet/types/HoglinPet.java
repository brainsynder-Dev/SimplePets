package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityHoglinPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

@SupportedVersion(version = ServerVersion.v1_16_R1)
public class HoglinPet extends PetType {
    public HoglinPet(PetCore plugin) {
        super(plugin, "hoglin", SoundMaker.ENTITY_HOGLIN_AMBIENT, EntityWrapper.HOGLIN);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/4409dc402a9fc3c7b892c44e5cd34a4a01d44419d05df8316f2e2d862ae0ba9c")
                .withName("&f&lHoglin Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityHoglinPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.HOGLIN;
    }
}
