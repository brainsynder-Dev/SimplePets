package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityEndermanPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class EndermanDefault extends PetDefault {
    public EndermanDefault(PetCore plugin) {
        super(plugin, "enderman", SoundMaker.ENTITY_ENDERMEN_AMBIENT, EntityWrapper.ENDERMAN);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.ENDER_PEARL).withName("&f&lEnderman Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityEndermanPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.SCREAM;
    }
}
