package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityHuskPet;
import simplepets.brainsynder.api.pet.IPet;


/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityDrowned}
 */
@Size(width = 0.6F, length = 1.8F)
public class EntityDrownedPet extends EntityZombiePet implements IEntityHuskPet {
    public EntityDrownedPet(World world, IPet pet) {
        super(EntityTypes.DROWNED, world, pet);
    }
    public EntityDrownedPet(World world) {
        super(EntityTypes.DROWNED, world);
    }
}
