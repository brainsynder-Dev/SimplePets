package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityWanderingTraderPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityVillagerTrader}
 */
public class EntityWanderingTraderPet extends EntityPet implements IEntityWanderingTraderPet {
    public EntityWanderingTraderPet(PetType type, PetUser user) {
        super(EntityType.WANDERING_TRADER, type, user);
    }
}
