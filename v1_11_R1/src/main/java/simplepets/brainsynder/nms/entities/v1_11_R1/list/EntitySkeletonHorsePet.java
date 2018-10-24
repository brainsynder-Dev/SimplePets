package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.World;
import simplepets.brainsynder.api.entity.passive.IEntitySkeletonHorsePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.branch.EntityHorseChestedAbstractPet;

public class EntitySkeletonHorsePet extends EntityHorseChestedAbstractPet implements IEntitySkeletonHorsePet {

    public EntitySkeletonHorsePet(World world) {
        super(world);
    }

    public EntitySkeletonHorsePet(World world, IPet pet) {
        super(world, pet);
    }
}
