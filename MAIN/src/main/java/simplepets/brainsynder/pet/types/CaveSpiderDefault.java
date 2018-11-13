package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityCaveSpiderPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class CaveSpiderDefault extends PetDefault {
    public CaveSpiderDefault(PetCore plugin) {
        super(plugin, "cave_spider", SoundMaker.ENTITY_SPIDER_AMBIENT, EntityWrapper.CAVE_SPIDER);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.FERMENTED_SPIDER_EYE).withName("&f&lCave Spider Pet");
    }

    @Override
    public PetData getPetData() {
        return PetData.SILENT;
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityCaveSpiderPet.class;
    }
}
