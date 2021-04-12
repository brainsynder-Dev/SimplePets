package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntityZoglinPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityAgeablePet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityZoglin}
 */
public class EntityZoglinPet extends EntityAgeablePet implements IEntityZoglinPet {
    public EntityZoglinPet(PetType type, PetUser user) {
        super(EntityTypes.ZOGLIN, type, user);
    }
}
