package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityWanderingTraderPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityVillagerTrader}
 */
@Size(width = 0.6F, length = 1.9F)
public class EntityWanderingTraderPet extends EntityPet implements IEntityWanderingTraderPet {
    public EntityWanderingTraderPet(World world, IPet pet) {
        super(EntityTypes.WANDERING_TRADER, world, pet);
    }
    public EntityWanderingTraderPet(World world) {
        super(EntityTypes.WANDERING_TRADER, world);
    }
}

