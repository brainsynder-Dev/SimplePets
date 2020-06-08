package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityVindicatorPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class VindicatorPet extends PetType {
    public VindicatorPet(PetCore plugin) {
        super(plugin, "vindicator", SoundMaker.ENTITY_VINDICATION_ILLAGER_AMBIENT, EntityWrapper.VINDICATOR);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/4f6fb89d1c631bd7e79fe185ba1a6705425f5c31a5ff626521e395d4a6f7e2")
                .withName("&f&lVindicator Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityVindicatorPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.JOHNNY;
    }
}
