package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntitySlimePet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class SlimeDefault extends PetDefault {
    public SlimeDefault(PetCore plugin) {
        super(plugin, "slime", SoundMaker.ENTITY_SLIME_SQUISH, EntityWrapper.SLIME);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SLIME_BALL).withName("&f&lSlime Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntitySlimePet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.SIZE;
    }
}
