package simplepets.brainsynder.pet.types;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.ServerVersion;
import simple.brainsynder.utils.SkullType;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityDonkeyPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class DonkeyDefault extends PetDefault {
    public DonkeyDefault(PetCore plugin) {
        super(plugin, "donkey", SoundMaker.ENTITY_DONKEY_AMBIENT, EntityWrapper.DONKEY);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return ItemBuilder.getSkull(SkullType.PLAYER)
                .setTexture("http://textures.minecraft.net/texture/db522f6d77c0696c9d1f2ad49bfa3cb8205a5e623af1c420bd740dc471914e97")
                .withName("&f&lDonkey Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityDonkeyPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.MULE_DONKEY;
    }

    @Override
    public ServerVersion getAllowedVersion() {
        return ServerVersion.v1_13_R1;
    }
}
