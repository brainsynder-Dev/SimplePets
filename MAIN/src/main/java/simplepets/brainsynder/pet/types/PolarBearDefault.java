package simplepets.brainsynder.pet.types;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityPolarBearPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class PolarBearDefault extends PetDefault {
    public PolarBearDefault(PetCore plugin) {
        super(plugin, "polarbear", SoundMaker.ENTITY_POLAR_BEAR_AMBIENT, EntityWrapper.POLAR_BEAR);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return ItemBuilder.getSkull(simple.brainsynder.utils.SkullType.PLAYER)
                .setTexture("http://textures.minecraft.net/texture/442123ac15effa1ba46462472871b88f1b09c1db467621376e2f71656d3fbc")
                .withName("&f&lPolar Bear Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityPolarBearPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.POLAR_BEAR;
    }
}
