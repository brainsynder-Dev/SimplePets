package simplepets.brainsynder.nms.entities.v1_13_R1.list;

import net.minecraft.server.v1_13_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntitySkeletonPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_13_R1.branch.EntitySkeletonAbstractPet;
import simplepets.brainsynder.nms.registry.v1_13_R1.Types;

@Size(width = 0.6F, length = 1.9F)
public class EntitySkeletonPet extends EntitySkeletonAbstractPet implements IEntitySkeletonPet {
    public EntitySkeletonPet(World world) {
        super(Types.SKELETON, world);
    }
    public EntitySkeletonPet(World world, IPet pet) {
        super(Types.SKELETON, world, pet);
    }
}
