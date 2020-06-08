package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityWolfPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class WolfPet extends PetType {
    public WolfPet(PetCore plugin) {
        super(plugin, "wolf", SoundMaker.ENTITY_WOLF_AMBIENT, EntityWrapper.WOLF);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.BONE).withName("&f&lWolf Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityWolfPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.WOLF;
    }
}
