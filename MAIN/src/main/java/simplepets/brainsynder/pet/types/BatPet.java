package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityBatPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class BatPet extends PetType {
    public BatPet(PetCore plugin) {
        super(plugin, "bat", SoundMaker.ENTITY_BAT_AMBIENT, EntityWrapper.BAT);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.FEATHER).withName("&f&lBat Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityBatPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.BAT;
    }

    @Override
    public boolean canFlyDefault() {
        return true;
    }
}
