package simplepets.brainsynder.nms.v1_16_R3.entities.list;

import net.minecraft.server.v1_16_R3.EntityCreature;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityPigZombiePet;
import simplepets.brainsynder.api.pet.IPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityPigZombie}
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
