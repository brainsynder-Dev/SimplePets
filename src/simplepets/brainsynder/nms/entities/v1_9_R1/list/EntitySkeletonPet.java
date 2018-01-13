package simplepets.brainsynder.nms.entities.v1_9_R1.list;

import net.minecraft.server.v1_9_R1.World;
import simplepets.brainsynder.api.entity.hostile.IEntitySkeletonPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_9_R1.EntityPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntitySkeletonPet extends EntityPet implements IEntitySkeletonPet {
    public EntitySkeletonPet(World world, IPet pet) {
        super(world, pet);
    }

}
