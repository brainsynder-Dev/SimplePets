package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityMooshroomPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class MooshroomDefault extends PetDefault {
    public MooshroomDefault(PetCore plugin) {
        super(plugin, "mooshroom", SoundMaker.ENTITY_COW_AMBIENT, EntityWrapper.MUSHROOM_COW);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.RED_MUSHROOM).withName("&f&lMooShroom Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityMooshroomPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.AGE;
    }
}
