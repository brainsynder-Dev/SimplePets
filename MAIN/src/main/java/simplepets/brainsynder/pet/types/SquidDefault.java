package simplepets.brainsynder.pet.types;

import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntitySquidPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class SquidDefault extends PetDefault {
    public SquidDefault(PetCore plugin) {
        super(plugin, "squid", SoundMaker.ENTITY_SQUID_AMBIENT, EntityWrapper.SQUID);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Utilities.fetchMaterial("INK_SACK", "INK_SAC")).withName("&f&lSquid Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntitySquidPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.SILENT;
    }
}
