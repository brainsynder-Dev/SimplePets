package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityBatPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class BatDefault extends PetDefault {
    public BatDefault(PetCore plugin) {
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
