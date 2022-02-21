package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityCaveSpiderPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityCaveSpider}
 */
public class EntityCaveSpiderPet extends EntityPet implements IEntityCaveSpiderPet {
    public EntityCaveSpiderPet(PetType type, PetUser user) {
        super(EntityType.CAVE_SPIDER, type, user);
    }
}
