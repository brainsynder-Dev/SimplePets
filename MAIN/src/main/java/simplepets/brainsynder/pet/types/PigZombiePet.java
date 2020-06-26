package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityPigZombiePet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

@SupportedVersion(version = ServerVersion.v1_16_R1)
public class PigZombiePet extends PetType {
    public PigZombiePet(PetCore plugin) {
        super(plugin, "zombified_piglin", SoundMaker.ENTITY_ZOMBIFIED_PIGLIN_AMBIENT, EntityWrapper.ZOMBIFIED_PIGLIN);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/7eabaecc5fae5a8a49c8863ff4831aaa284198f1a2398890c765e0a8de18da8c")
                .withName("&f&lZombified Piglin Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityPigZombiePet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.ZOMBIE_PIGLIN;
    }
}
