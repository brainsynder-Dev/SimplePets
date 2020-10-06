package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityBlazePet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.AdditionalData;
import simplepets.brainsynder.wrapper.EntityWrapper;

@AdditionalData(passive = false)
public class BlazePet extends PetType {
    public BlazePet(PetCore plugin) {
        super(plugin, "blaze", SoundMaker.ENTITY_BLAZE_AMBIENT, EntityWrapper.BLAZE);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.BLAZE_ROD).withName("&f&lBlaze Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityBlazePet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.BLAZE;
    }

    @Override
    public boolean canFlyDefault() {
        return true;
    }
}
