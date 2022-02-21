package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityEndermitePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityEndermite}
 */
public class EntityEndermitePet extends EntityPet implements IEntityEndermitePet {
    public EntityEndermitePet(PetType type, PetUser user) {
        super(EntityType.ENDERMITE, type, user);
    }
}
