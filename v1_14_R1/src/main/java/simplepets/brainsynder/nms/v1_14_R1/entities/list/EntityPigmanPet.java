package simplepets.brainsynder.nms.v1_14_R1.entities.list;

import net.minecraft.server.v1_14_R1.EntityCreature;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityPigmanPet;
import simplepets.brainsynder.api.pet.IPet;

/**
 * NMS: {@link net.minecraft.server.v1_14_R1.EntityPigZombie}
 */
@Size(width = 0.6F, length = 1.8F)
public class EntityPigmanPet extends EntityZombiePet implements IEntityPigmanPet {
    public EntityPigmanPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityPigmanPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
}
