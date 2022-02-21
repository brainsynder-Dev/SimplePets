package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityZoglinPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityZoglin}
 */
public class EntityZoglinPet extends EntityAgeablePet implements IEntityZoglinPet {
    public EntityZoglinPet(PetType type, PetUser user) {
        super(EntityType.ZOGLIN, type, user);
    }
}
