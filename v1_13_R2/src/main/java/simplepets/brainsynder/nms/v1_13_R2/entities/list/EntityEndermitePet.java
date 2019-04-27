package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityEndermitePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_13_R2.EntityEndermite}
 */
@Size(width = 0.4F, length = 0.3F)
public class EntityEndermitePet extends EntityPet implements IEntityEndermitePet {
    public EntityEndermitePet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityEndermitePet(EntityTypes<?> type, World world) {
        super(type, world);
    }
}
