package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntitySilverfishPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class SilverfishPet extends PetType {
    public SilverfishPet(PetCore plugin) {
        super(plugin, "silverfish", SoundMaker.ENTITY_SILVERFISH_AMBIENT, EntityWrapper.SILVERFISH);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/d06310a8952b265c6e6bed4348239ddea8e5482c8c68be6fff981ba8056bf2e")
                .withName("&f&lSilverfish Pet");
    }

    @Override
    public PetData getPetData() {
        return PetData.SILENT;
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntitySilverfishPet.class;
    }
}
