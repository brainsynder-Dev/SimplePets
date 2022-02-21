package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityElderGuardianPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityGuardianElder}
 */
public class EntityElderGuardianPet extends EntityGuardianPet implements IEntityElderGuardianPet {
    public EntityElderGuardianPet(PetType type, PetUser user) {
        super(EntityType.ELDER_GUARDIAN, type, user);
    }
}
