package simplepets.brainsynder.pet.types;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityPiglinBrutePet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.AdditionalData;
import simplepets.brainsynder.wrapper.EntityWrapper;

@AdditionalData(passive = false)
@SupportedVersion(version = ServerVersion.v1_16_R2)
public class PiglinBrutePet extends PetType {
    public PiglinBrutePet(PetCore plugin) {
        super(plugin, "piglin_brute", SoundMaker.ENTITY_PIGLIN_BRUTE_AMBIENT, EntityWrapper.PIGLIN_BRUTE);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/3e300e9027349c4907497438bac29e3a4c87a848c50b34c21242727b57f4e1cf")
                .withName("&f&lPiglin Brute Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityPiglinBrutePet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.SHAKING;
    }
}
