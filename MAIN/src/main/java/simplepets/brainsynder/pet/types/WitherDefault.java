package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityWitherPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class WitherDefault extends PetDefault {
    public WitherDefault(PetCore plugin) {
        super(plugin, "wither", SoundMaker.ENTITY_WITHER_AMBIENT, EntityWrapper.WITHER);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.NETHER_STAR).withName("&f&lWither Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityWitherPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.WITHER;
    }
}
