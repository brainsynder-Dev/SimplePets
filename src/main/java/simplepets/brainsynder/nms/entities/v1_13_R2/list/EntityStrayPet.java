package simplepets.brainsynder.nms.entities.v1_13_R2.list;

import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityStrayPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_13_R2.branch.EntitySkeletonAbstractPet;
import simplepets.brainsynder.nms.registry.v1_13_R2.Types;

@Size(width = 0.6F, length = 1.9F)
public class EntityStrayPet extends EntitySkeletonAbstractPet implements IEntityStrayPet {
    public EntityStrayPet(World world, IPet pet) {
        super(Types.STRAY, world, pet);
    }
    public EntityStrayPet(World world) {
        super(Types.STRAY, world);
    }
}

