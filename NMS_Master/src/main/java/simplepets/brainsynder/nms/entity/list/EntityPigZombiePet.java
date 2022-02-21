package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityPigZombiePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityPigZombie}
 */
public class EntityPigZombiePet extends EntityZombiePet implements IEntityPigZombiePet {
    public EntityPigZombiePet(PetType type, PetUser user) {
        super(EntityType.ZOMBIFIED_PIGLIN, type, user);
    }
}
