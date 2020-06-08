package simplepets.brainsynder.pet.types;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Material;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntitySkeletonPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class SkeletonPet extends PetType {
    public SkeletonPet(PetCore plugin) {
        super(plugin, "skeleton", SoundMaker.ENTITY_SKELETON_AMBIENT, EntityWrapper.SKELETON);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.SKELETON_SKULL).withName("&f&lSkeleton Pet");
    }

    @Override
    public Class<? extends IEntityPet> getEntityClass() {
        return IEntitySkeletonPet.class;
    }

    @Override
    public PetData getPetData() {
        return PetData.ARMS;
    }
}
