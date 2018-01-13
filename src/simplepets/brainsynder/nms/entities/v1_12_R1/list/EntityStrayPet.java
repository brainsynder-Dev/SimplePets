package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.api.entity.hostile.IEntityStrayPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.branch.EntitySkeletonAbstractPet;
import simplepets.brainsynder.utils.Size;

@Size(width = 0.6F, length = 1.9F)
public class EntityStrayPet extends EntitySkeletonAbstractPet implements IEntityStrayPet {
    public EntityStrayPet(World world, IPet pet) {
        super(world, pet);
    }
    public EntityStrayPet(World world) {
        super(world);
    }
}
