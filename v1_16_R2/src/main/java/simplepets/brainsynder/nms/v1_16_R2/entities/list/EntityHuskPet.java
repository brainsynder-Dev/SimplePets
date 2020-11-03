package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityHuskPet;
import simplepets.brainsynder.api.pet.IPet;


/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityZombieHusk}
 */
@Size(width = 0.6F, length = 1.8F)
public class EntityHuskPet extends EntityZombiePet implements IEntityHuskPet {
    public EntityHuskPet(World world, IPet pet) {
        super(EntityTypes.HUSK, world, pet);
    }
    public EntityHuskPet(World world) {
        super(EntityTypes.HUSK, world);
    }
}