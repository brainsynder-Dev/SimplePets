package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityWitherSkeletonPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.branch.EntitySkeletonAbstractPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntitySkeletonWither}
 */
@Size(width = 0.7F, length = 2.4F)
public class EntityWitherSkeletonPet extends EntitySkeletonAbstractPet implements IEntityWitherSkeletonPet {
    public EntityWitherSkeletonPet(World world) {
        super(EntityTypes.WITHER_SKELETON, world);
    }
    public EntityWitherSkeletonPet(World world, IPet pet) {
        super(EntityTypes.WITHER_SKELETON, world, pet);
    }
}
