package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityPufferFishPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

@SupportedVersion(version = ServerVersion.v1_13_R1)
public class PufferFishPet extends PetType {
    public PufferFishPet(PetCore plugin) {
        super(plugin, "puffer_fish", SoundMaker.ENTITY_PUFFER_FISH_AMBIENT, EntityWrapper.PUFFER_FISH);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/17152876bc3a96dd2a2299245edb3beef647c8a56ac8853a687c3e7b5d8bb")
                .withName("&f&lPufferFish Pet");
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
