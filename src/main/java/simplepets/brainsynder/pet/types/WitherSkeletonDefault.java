package simplepets.brainsynder.pet.types;

import org.bukkit.Material;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityWitherSkeletonPet;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class WitherSkeletonDefault extends PetDefault {
    public WitherSkeletonDefault(PetCore plugin) {
        super(plugin, "wither_skeleton", SoundMaker.ENTITY_SKELETON_AMBIENT, EntityWrapper.WITHER_SKELETON);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SKULL_ITEM).withData(1).withName("&f&lWither Skeleton Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityWitherSkeletonPet.class;
    }
}
