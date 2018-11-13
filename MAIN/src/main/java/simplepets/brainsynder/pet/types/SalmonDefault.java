package simplepets.brainsynder.pet.types;

import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntitySalmonPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class SalmonDefault extends PetDefault {
    public SalmonDefault(PetCore plugin) {
        super(plugin, "salmon", SoundMaker.ENTITY_SALMON_AMBIENT, EntityWrapper.SALMON);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Utilities.fetchMaterial("SALMON")).withName("&f&lSalmon Pet");
    }

    @Override
    public PetData getPetData() {
        return PetData.SILENT;
    }

    @Override
    public ServerVersion getAllowedVersion() {
        return ServerVersion.v1_13_R1;
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntitySalmonPet.class;
    }
}
