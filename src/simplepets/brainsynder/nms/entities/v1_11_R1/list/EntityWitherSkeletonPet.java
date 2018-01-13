package simplepets.brainsynder.nms.entities.v1_11_R1.list;


import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.api.entity.hostile.IEntityWitherSkeletonPet;
import simplepets.brainsynder.api.pet.IPet;

public class EntityWitherSkeletonPet extends EntitySkeletonPet implements IEntityWitherSkeletonPet {

    public EntityWitherSkeletonPet(World world) {
        super(world);
    }

    public EntityWitherSkeletonPet(World world, IPet pet) {
        super(world, pet);
    }
}
