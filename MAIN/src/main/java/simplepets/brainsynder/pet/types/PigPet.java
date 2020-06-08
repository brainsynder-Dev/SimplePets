package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityPigPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class PigPet extends PetType {
    public PigPet(PetCore plugin) {
        super(plugin, "pig", SoundMaker.ENTITY_PIG_AMBIENT, EntityWrapper.PIG);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PORKCHOP).withName("&f&lPig Pet");
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
