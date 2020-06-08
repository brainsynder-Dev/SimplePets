package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityBeePet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

@SupportedVersion(version = ServerVersion.v1_15_R1)
public class BeePet extends PetType {
    public BeePet(PetCore plugin) {
        super(plugin, "bee", SoundMaker.ENTITY_BEE_LOOP, EntityWrapper.BEE);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/fa2cb74c13245d3ce9bacc8b1600af02fd7c91f501feaf97364e1f8b6f04f47f")
                .withName("&f&lBee Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityBeePet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.BEE;
    }

    @Override
    public boolean canFlyDefault() {
        return true;
    }
}
