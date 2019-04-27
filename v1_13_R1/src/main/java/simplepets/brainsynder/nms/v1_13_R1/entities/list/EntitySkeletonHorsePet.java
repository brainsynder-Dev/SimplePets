package simplepets.brainsynder.nms.v1_13_R1.entities.list;

import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntitySkeletonHorsePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R1.entities.branch.EntityHorseChestedAbstractPet;
import simplepets.brainsynder.wrapper.EntityWrapper;

@Size(width = 1.4F, length = 1.6F)
public class EntitySkeletonHorsePet extends EntityHorseChestedAbstractPet implements IEntitySkeletonHorsePet {
    public EntitySkeletonHorsePet(EntityTypes<?> type, World world) {
        super(type, world);
    }
    public EntitySkeletonHorsePet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    public EntityWrapper getPetEntityType() {
        return EntityWrapper.SKELETON_HORSE;
    }
}
