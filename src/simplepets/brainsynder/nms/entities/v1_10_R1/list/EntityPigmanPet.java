package simplepets.brainsynder.nms.entities.v1_10_R1.list;

import net.minecraft.server.v1_10_R1.World;
import simplepets.brainsynder.api.entity.hostile.IEntityPigmanPet;
import simplepets.brainsynder.api.pet.IPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityPigmanPet extends EntityZombiePet implements IEntityPigmanPet {
    public EntityPigmanPet(World world, IPet pet) {
        super(world, pet);
    }

}
