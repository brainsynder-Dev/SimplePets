package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityPiglinBrutePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.branch.EntityPiglinAbstractPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityPiglinBrute}
 */
public class EntityPiglinBrutePet extends EntityPiglinAbstractPet implements IEntityPiglinBrutePet {
    public EntityPiglinBrutePet(PetType type, PetUser user) {
        super(EntityType.PIGLIN_BRUTE, type, user);
    }
}
