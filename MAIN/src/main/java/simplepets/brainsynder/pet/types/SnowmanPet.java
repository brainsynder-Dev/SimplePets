package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntitySnowmanPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class SnowmanPet extends PetType {
    public SnowmanPet(PetCore plugin) {
        super(plugin, "snowman", SoundMaker.ENTITY_SNOWMAN_AMBIENT, EntityWrapper.SNOWMAN);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.PUMPKIN).withName("&f&lSnowman Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntitySnowmanPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.PUMPKIN;
    }
}
