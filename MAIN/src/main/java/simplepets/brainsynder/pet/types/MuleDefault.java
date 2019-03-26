package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityMulePet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class MuleDefault extends PetDefault {
    public MuleDefault(PetCore plugin) {
        super(plugin, "mule", SoundMaker.ENTITY_MULE_AMBIENT, EntityWrapper.MULE);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SADDLE).withName("&f&lMule Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityMulePet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.MULE;
    }
}
