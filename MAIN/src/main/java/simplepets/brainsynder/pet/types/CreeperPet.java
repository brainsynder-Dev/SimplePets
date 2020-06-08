package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityCreeperPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class CreeperPet extends PetType {
    public CreeperPet(PetCore plugin) {
        super(plugin, "creeper", SoundMaker.ENTITY_CREEPER_HURT, EntityWrapper.CREEPER);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.GUNPOWDER).withName("&f&lCreeper Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityCreeperPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.POWERED;
    }
}
