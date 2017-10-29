package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntityStrayPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.branch.EntitySkeletonAbstractPet;
import simplepets.brainsynder.pet.IPet;

public class EntityStrayPet extends EntitySkeletonAbstractPet implements IEntityStrayPet {
    public EntityStrayPet(World world, IPet pet) {
        super(world, pet);
    }
    public EntityStrayPet(World world) {
        super(world);
    }
}
