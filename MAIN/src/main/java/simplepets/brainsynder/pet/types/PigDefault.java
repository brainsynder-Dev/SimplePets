package simplepets.brainsynder.pet.types;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityPigPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class PigDefault extends PetDefault {
    public PigDefault(PetCore plugin) {
        super(plugin, "pig", SoundMaker.ENTITY_PIG_AMBIENT, EntityWrapper.PIG);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Utilities.fetchMaterial("PORK", "PORKCHOP")).withName("&f&lPig Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityPigPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.PIG;
    }
}
