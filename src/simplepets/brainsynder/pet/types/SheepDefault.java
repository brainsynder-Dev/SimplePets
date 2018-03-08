package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntitySheepPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class SheepDefault extends PetDefault {
    public SheepDefault(PetCore plugin) {
        super(plugin, "sheep", SoundMaker.ENTITY_SHEEP_AMBIENT, EntityWrapper.SHEEP);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.WOOL).withName("&f&lSheep Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntitySheepPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.SHEEP;
    }
}
