package simplepets.brainsynder.nms.entities.v1_9_R2.list;

import net.minecraft.server.v1_9_R2.World;
import simplepets.brainsynder.api.entity.hostile.IEntityEndermitePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_9_R2.EntityPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityEndermitePet extends EntityPet implements IEntityEndermitePet {
    public EntityEndermitePet(World world, IPet pet) {
        super(world, pet);
    }

}
