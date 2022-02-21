package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntitySkeletonPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.branch.EntitySkeletonAbstractPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntitySkeleton}
 */
public class EntitySkeletonPet extends EntitySkeletonAbstractPet implements IEntitySkeletonPet {
    public EntitySkeletonPet(PetType type, PetUser user) {
        super(EntityType.SKELETON, type, user);
    }
}
