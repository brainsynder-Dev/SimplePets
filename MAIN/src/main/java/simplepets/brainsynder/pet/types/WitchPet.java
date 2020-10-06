package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityWitchPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.AdditionalData;
import simplepets.brainsynder.wrapper.EntityWrapper;

@AdditionalData(passive = false)
public class WitchPet extends PetType {
    public WitchPet(PetCore plugin) {
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
