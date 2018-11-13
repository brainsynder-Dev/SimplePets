package simplepets.brainsynder.pet.types;

import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityEndermitePet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class EndermiteDefault extends PetDefault {
    public EndermiteDefault(PetCore plugin) {
        super(plugin, "endermite", SoundMaker.ENTITY_ENDERMITE_AMBIENT, EntityWrapper.ENDERMITE);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Utilities.fetchMaterial("EYE_OF_ENDER", "ENDER_EYE")).withName("&f&lEndermite Pet");
    }

    @Override
    public PetData getPetData() {
        return PetData.SILENT;
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityEndermitePet.class;
    }
}
