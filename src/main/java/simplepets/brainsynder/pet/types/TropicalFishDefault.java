package simplepets.brainsynder.pet.types;

import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityTropicalFishPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class TropicalFishDefault extends PetDefault {
    public TropicalFishDefault(PetCore plugin) {
        super(plugin, "tropical_fish", SoundMaker.ENTITY_BAT_LOOP, EntityWrapper.TROPICAL_FISH);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return Utilities.getSkullMaterial(Utilities.SkullType.PLAYER).toBuilder(1)
                .setTexture("http://textures.minecraft.net/texture/36d149e4d499929672e2768949e6477959c21e65254613b327b538df1e4df")
                .withName("&f&lTropicalFish Pet");
    }

    @Override
    public ServerVersion getAllowedVersion() {
        return ServerVersion.v1_13_R1;
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityTropicalFishPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.TROPICAL_FISH;
    }
}
