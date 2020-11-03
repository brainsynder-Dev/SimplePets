package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityCaveSpiderPet;
import simplepets.brainsynder.api.pet.IPet;


/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityCaveSpider}
 */
@Size(width = 0.7F, length = 0.5F)
public class EntityCaveSpiderPet extends EntitySpiderPet implements IEntityCaveSpiderPet {
    public EntityCaveSpiderPet(World world, IPet pet) {
        super(EntityTypes.CAVE_SPIDER, world, pet);
    }
    public EntityCaveSpiderPet(World world) {
        super(EntityTypes.CAVE_SPIDER, world);
    }
}
