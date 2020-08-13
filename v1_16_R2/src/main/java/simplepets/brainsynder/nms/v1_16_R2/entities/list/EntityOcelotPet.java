package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityCreature;
import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityOcelotPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.EntityTameablePet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityOcelot}
 */
@Size(width = 0.6F, length = 0.8F)
public class EntityOcelotPet extends EntityTameablePet implements IEntityOcelotPet {
    public EntityOcelotPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
    public EntityOcelotPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
}
