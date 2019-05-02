package simplepets.brainsynder.nms.v1_14_R1.entities.list;

import net.minecraft.server.v1_14_R1.EntityCreature;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityIronGolemPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_14_R1.entities.EntityPet;


/**
 * NMS: {@link net.minecraft.server.v1_14_R1.EntityIronGolem}
 */
@Size(width = 1.4F, length = 2.9F)
public class EntityIronGolemPet extends EntityPet implements IEntityIronGolemPet {
    public EntityIronGolemPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityIronGolemPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
}
