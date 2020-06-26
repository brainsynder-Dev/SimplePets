package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityStriderPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

@SupportedVersion(version = ServerVersion.v1_16_R1)
public class StriderPet extends PetType {
    public StriderPet(PetCore plugin) {
        super(plugin, "strider", SoundMaker.ENTITY_STRIDER_AMBIENT, EntityWrapper.STRIDER);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/18a9adf780ec7dd4625c9c0779052e6a15a451866623511e4c82e9655714b3c1")
                .withName("&f&lStrider Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityStriderPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.STRIDER;
    }
}
