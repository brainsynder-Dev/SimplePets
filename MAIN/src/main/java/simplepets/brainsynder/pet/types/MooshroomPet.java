package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityMooshroomPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class MooshroomPet extends PetType {
    public MooshroomPet(PetCore plugin) {
        super(plugin, "mooshroom", SoundMaker.ENTITY_COW_AMBIENT, EntityWrapper.MUSHROOM_COW);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.RED_MUSHROOM).withName("&f&lMooshroom Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityMooshroomPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.MOOSHROOM;
    }
}
