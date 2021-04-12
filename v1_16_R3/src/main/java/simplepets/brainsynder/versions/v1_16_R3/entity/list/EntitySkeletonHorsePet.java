package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntitySkeletonHorsePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.branch.EntityHorseAbstractPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityHorseSkeleton}
 */
public class EntitySkeletonHorsePet extends EntityHorseAbstractPet implements IEntitySkeletonHorsePet {
    public EntitySkeletonHorsePet(PetType type, PetUser user) {
        super(EntityTypes.SKELETON_HORSE, type, user);
    }
}
