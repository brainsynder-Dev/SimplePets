package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityTropicalFishPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

@SupportedVersion(version = ServerVersion.v1_13_R1)
public class TropicalFishPet extends PetType {
    public TropicalFishPet(PetCore plugin) {
        super(plugin, "tropical_fish", SoundMaker.ENTITY_TROPICAL_FISH_AMBIENT, EntityWrapper.TROPICAL_FISH);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/36d149e4d499929672e2768949e6477959c21e65254613b327b538df1e4df")
                .withName("&f&lTropicalFish Pet");
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
