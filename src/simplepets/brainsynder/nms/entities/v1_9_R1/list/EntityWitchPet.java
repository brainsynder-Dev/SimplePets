package simplepets.brainsynder.nms.entities.v1_9_R1.list;

import net.minecraft.server.v1_9_R1.World;
import simplepets.brainsynder.api.entity.hostile.IEntityWitchPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_9_R1.EntityPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityWitchPet extends EntityPet implements IEntityWitchPet {
    public EntityWitchPet(World world, IPet pet) {
        super(world, pet);
    }

}
