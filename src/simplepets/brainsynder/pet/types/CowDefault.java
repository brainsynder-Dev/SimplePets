package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityCowPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class CowDefault extends PetDefault {
    public CowDefault(PetCore plugin) {
        super(plugin, "cow", SoundMaker.ENTITY_COW_AMBIENT, EntityWrapper.COW);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.COOKED_BEEF).withName("&f&lCow Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityCowPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.AGE;
    }
}
