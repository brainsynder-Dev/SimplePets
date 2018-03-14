package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntityWitherSkeletonPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.utils.Size;

@Size(width = 0.7F, length = 2.4F)
public class EntityWitherSkeletonPet extends EntitySkeletonPet implements IEntityWitherSkeletonPet {
    public EntityWitherSkeletonPet(World world) {
        super(world);
    }
    public EntityWitherSkeletonPet(World world, IPet pet) {
        super(world, pet);
    }
}
