package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityWitherSkeletonPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class WitherSkeletonDefault extends PetDefault {
    public WitherSkeletonDefault(PetCore plugin) {
        super(plugin, "wither_skeleton", SoundMaker.ENTITY_SKELETON_AMBIENT, EntityWrapper.WITHER_SKELETON);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.WITHER_SKELETON_SKULL).withName("&f&lWither Skeleton Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntityWitherSkeletonPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.ARMS;
    }
}
