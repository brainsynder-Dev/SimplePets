package simplepets.brainsynder.nms.v1_14_R1.entities.list;

import net.minecraft.server.v1_14_R1.EntityCreature;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityHuskPet;
import simplepets.brainsynder.api.pet.IPet;


/**
 * NMS: {@link net.minecraft.server.v1_14_R1.EntityZombieHusk}
 */
@Size(width = 0.6F, length = 1.8F)
public class EntityHuskPet extends EntityZombiePet implements IEntityHuskPet {
    public EntityHuskPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityHuskPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
}