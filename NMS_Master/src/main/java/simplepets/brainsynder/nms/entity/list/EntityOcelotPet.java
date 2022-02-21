package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityOcelotPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityOcelot}
 */
public class EntityOcelotPet extends EntityAgeablePet implements IEntityOcelotPet {
    public EntityOcelotPet(PetType type, PetUser user) {
        super(EntityType.OCELOT, type, user);
    }
}
