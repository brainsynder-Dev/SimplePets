package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityCowPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.AgeableEntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_13_R2.EntityCow}
 */
@Size(width = 0.9F, length = 1.3F)
public class EntityCowPet extends AgeableEntityPet implements IEntityCowPet {
    public EntityCowPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityCowPet(EntityTypes<?> type, World world) {
        super(type, world);
    }
}
