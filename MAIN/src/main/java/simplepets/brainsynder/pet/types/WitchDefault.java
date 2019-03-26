package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityWitchPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class WitchDefault extends PetDefault {
    public WitchDefault(PetCore plugin) {
        super(plugin, "witch", SoundMaker.ENTITY_WITCH_AMBIENT, EntityWrapper.WITCH);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.POTION).withName("&f&lWitch Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityWitchPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.POTION;
    }
}
