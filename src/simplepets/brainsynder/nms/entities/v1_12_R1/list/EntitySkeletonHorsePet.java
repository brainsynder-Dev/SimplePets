package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntitySkeletonHorsePet;
import simplepets.brainsynder.nms.entities.v1_12_R1.branch.EntityHorseChestedAbstractPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class EntitySkeletonHorsePet extends EntityHorseChestedAbstractPet implements IEntitySkeletonHorsePet {

    public EntitySkeletonHorsePet(World world) {
        super(world);
    }

    public EntitySkeletonHorsePet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public EntityWrapper getEntityType() {
        return EntityWrapper.SKELETON_HORSE;
    }
}
