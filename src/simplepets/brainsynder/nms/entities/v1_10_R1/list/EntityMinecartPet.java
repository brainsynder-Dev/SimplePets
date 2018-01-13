package simplepets.brainsynder.nms.entities.v1_10_R1.list;

import net.minecraft.server.v1_10_R1.World;
import simplepets.brainsynder.api.entity.hostile.IEntityGiantPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_10_R1.EntityPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityMinecartPet extends EntityPet implements IEntityGiantPet {
    public EntityMinecartPet(World world, IPet pet) {
        super(world, pet);
    }

}
