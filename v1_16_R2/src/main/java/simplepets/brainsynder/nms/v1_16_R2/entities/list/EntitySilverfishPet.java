package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntitySilverfishPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntitySilverfish}
 */
@Size(width = 0.3F, length = 0.7F)
public class EntitySilverfishPet extends EntityPet implements IEntitySilverfishPet {
    public EntitySilverfishPet(World world) {
        super(EntityTypes.SILVERFISH, world);
    }
    public EntitySilverfishPet(World world, IPet pet) {
        super(EntityTypes.SILVERFISH, world, pet);
    }
}
