package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityPigmanPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class PigmanPet extends PetType {
    public PigmanPet(PetCore plugin) {
        super(plugin, "pigman", SoundMaker.ENTITY_ZOMBIE_PIG_AMBIENT, EntityWrapper.PIG_ZOMBIE);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/74e9c6e98582ffd8ff8feb3322cd1849c43fb16b158abb11ca7b42eda7743eb")
                .withName("&f&lPigman Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityPigmanPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.ZOMBIE;
    }
}
