package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntitySilverfishPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntitySilverfish}
 */
public class EntitySilverfishPet extends EntityPet implements IEntitySilverfishPet {
    public EntitySilverfishPet(PetType type, PetUser user) {
        super(EntityType.SILVERFISH, type, user);
    }
}
