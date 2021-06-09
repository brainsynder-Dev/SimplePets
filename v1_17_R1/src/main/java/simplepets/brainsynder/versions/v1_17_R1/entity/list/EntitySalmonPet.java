package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntitySalmonPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityFishPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntitySalmon}
 */
public class EntitySalmonPet extends EntityFishPet implements IEntitySalmonPet {
    public EntitySalmonPet(PetType type, PetUser user) {
        super(EntityTypes.SALMON, type, user);
    }
}
