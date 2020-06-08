package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityDonkeyPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

@SupportedVersion(version = ServerVersion.v1_13_R1)
public class DonkeyPet extends PetType {
    public DonkeyPet(PetCore plugin) {
        super(plugin, "donkey", SoundMaker.ENTITY_DONKEY_AMBIENT, EntityWrapper.DONKEY);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder (org.bukkit.Material.PLAYER_HEAD)
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
}
