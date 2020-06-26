package simplepets.brainsynder.nms.v1_16_R1.entities.list;

import net.minecraft.server.v1_16_R1.EntityCreature;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityPigZombiePet;
import simplepets.brainsynder.api.pet.IPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R1.EntityPigZombie}
 */
@Size(width = 0.6F, length = 1.8F)
public class EntityPigZombiePet extends EntityZombiePet implements IEntityPigZombiePet {
    public EntityPigZombiePet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityPigZombiePet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
}
