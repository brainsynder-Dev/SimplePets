package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntitySlimePet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.AdditionalData;
import simplepets.brainsynder.wrapper.EntityWrapper;

@AdditionalData(passive = false)
public class SlimePet extends PetType {
    public SlimePet(PetCore plugin) {
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
