package simplepets.brainsynder.nms.v1_15_R1.entities.list;

import net.minecraft.server.v1_15_R1.EntityCreature;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntitySpiderPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_15_R1.entities.EntityPet;


/**
 * NMS: {@link net.minecraft.server.v1_15_R1.EntitySpider}
 */
@Size(width = 1.4F, length = 0.9F)
public class EntitySpiderPet extends EntityPet implements IEntitySpiderPet {
    public EntitySpiderPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntitySpiderPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
}
