package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityHuskPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityZombieHusk}
 */
public class EntityHuskPet extends EntityZombiePet implements IEntityHuskPet {
    public EntityHuskPet(PetType type, PetUser user) {
        super(EntityType.HUSK, type, user);
    }
}
