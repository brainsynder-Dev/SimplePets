package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityWitherSkeletonPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.branch.EntitySkeletonAbstractPet;
import simplepets.brainsynder.nms.v1_13_R2.registry.Types;

@Size(width = 0.7F, length = 2.4F)
public class EntityWitherSkeletonPet extends EntitySkeletonAbstractPet implements IEntityWitherSkeletonPet {
    public EntityWitherSkeletonPet(World world) {
        super(Types.WITHER_SKELETON, world);
    }
    public EntityWitherSkeletonPet(World world, IPet pet) {
        super(Types.WITHER_SKELETON, world, pet);
    }
}
