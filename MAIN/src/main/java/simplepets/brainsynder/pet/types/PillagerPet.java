package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityPillagerPet;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

@SupportedVersion(version = ServerVersion.v1_14_R1)
public class PillagerPet extends PetType {
    public PillagerPet(PetCore plugin) {
        super(plugin, "pillager", SoundMaker.ENTITY_PILLAGER_AMBIENT, EntityWrapper.PILLAGER);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/8fd4983e30b277f0b97b7d8c6f8a0358201be226a2c55e2a0d390c3942ec2df5")
                .withName("&f&lPillager Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityPillagerPet.class;
    }
}
