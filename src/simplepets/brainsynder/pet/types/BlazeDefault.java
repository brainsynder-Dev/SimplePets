package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityBlazePet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class BlazeDefault extends PetDefault {
    public BlazeDefault(PetCore plugin) {
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
