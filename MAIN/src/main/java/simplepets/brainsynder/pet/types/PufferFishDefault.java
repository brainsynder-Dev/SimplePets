package simplepets.brainsynder.pet.types;

import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityPufferFishPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class PufferFishDefault extends PetDefault {
    public PufferFishDefault(PetCore plugin) {
        super(plugin, "puffer_fish", SoundMaker.ENTITY_PUFFER_FISH_AMBIENT, EntityWrapper.PUFFER_FISH);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return Utilities.getSkullMaterial(Utilities.SkullType.PLAYER).toBuilder(1)
                .setTexture("http://textures.minecraft.net/texture/17152876bc3a96dd2a2299245edb3beef647c8a56ac8853a687c3e7b5d8bb")
                .withName("&f&lPufferFish Pet");
    }

    @Override
    public ServerVersion getAllowedVersion() {
        return ServerVersion.v1_13_R1;
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityPufferFishPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.PUFFER_SIZE;
    }
}
