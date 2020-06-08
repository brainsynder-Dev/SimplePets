package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityChickenPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class ChickenPet extends PetType {
    public ChickenPet(PetCore plugin) {
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
