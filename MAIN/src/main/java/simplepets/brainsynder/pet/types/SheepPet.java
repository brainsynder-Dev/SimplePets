package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntitySheepPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class SheepPet extends PetType {
    public SheepPet(PetCore plugin) {
        super(plugin, "sheep", SoundMaker.ENTITY_SHEEP_AMBIENT, EntityWrapper.SHEEP);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.WHITE_WOOL).withName("&f&lSheep Pet");
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
