package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityChickenPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class ChickenDefault extends PetDefault {
    public ChickenDefault(PetCore plugin) {
        super(plugin, "chicken", SoundMaker.ENTITY_CHICKEN_AMBIENT, EntityWrapper.CHICKEN);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.COOKED_CHICKEN).withName("&f&lChicken Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityChickenPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.AGE;
    }
}
