package simplepets.brainsynder.pet.types;

import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntitySkeletonPet;
import simplepets.brainsynder.pet.PetData;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class SkeletonDefault extends PetDefault {
    public SkeletonDefault(PetCore plugin) {
        super(plugin, "skeleton", SoundMaker.ENTITY_SKELETON_AMBIENT, EntityWrapper.SKELETON);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return Utilities.getSkullMaterial(Utilities.SkullType.SKELETON).toBuilder(1).withName("&f&lSkeleton Pet");
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
