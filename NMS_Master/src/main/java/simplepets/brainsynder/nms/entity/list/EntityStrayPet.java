package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityStrayPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.branch.EntitySkeletonAbstractPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntitySkeletonStray}
 */
public class EntityStrayPet extends EntitySkeletonAbstractPet implements IEntityStrayPet {
    public EntityStrayPet(PetType type, PetUser user) {
        super(EntityType.STRAY, type, user);
    }
}
