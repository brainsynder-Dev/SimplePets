package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityGiantPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityGiantZombie}
 */
public class EntityGiantPet extends EntityPet implements IEntityGiantPet {
    public EntityGiantPet(PetType type, PetUser user) {
        super(EntityType.GIANT, type, user);
    }
}
