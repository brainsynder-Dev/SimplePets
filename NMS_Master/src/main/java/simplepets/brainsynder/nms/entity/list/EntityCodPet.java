package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityCodPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityFishPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityCod}
 */
public class EntityCodPet extends EntityFishPet implements IEntityCodPet {
    public EntityCodPet(PetType type, PetUser user) {
        super(EntityType.COD, type, user);
    }
}
