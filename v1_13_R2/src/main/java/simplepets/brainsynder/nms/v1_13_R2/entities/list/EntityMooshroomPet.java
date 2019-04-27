package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityMooshroomPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.AgeableEntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_13_R2.EntityMushroomCow}
 */
@Size(width = 0.9F, length = 1.3F)
public class EntityMooshroomPet extends AgeableEntityPet implements IEntityMooshroomPet {
    public EntityMooshroomPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityMooshroomPet(EntityTypes<?> type, World world) {
        super(type, world);
    }
}
