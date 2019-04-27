package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityGiantPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_13_R2.EntityGiantZombie}
 */
@Size(width = 5.5F, length = 5.5F)
public class EntityGiantPet extends EntityPet implements IEntityGiantPet {
    public EntityGiantPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityGiantPet(EntityTypes<?> type, World world) {
        super(type, world);
    }
}
