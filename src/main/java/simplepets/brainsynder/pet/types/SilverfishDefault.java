package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntitySilverfishPet;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class SilverfishDefault extends PetDefault {
    public SilverfishDefault(PetCore plugin) {
        super(plugin, "silverfish", SoundMaker.ENTITY_SILVERFISH_AMBIENT, EntityWrapper.SILVERFISH);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/d06310a8952b265c6e6bed4348239ddea8e5482c8c68be6fff981ba8056bf2e")
                .withName("&f&lSilverfish Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntitySilverfishPet.class;
    }
}
