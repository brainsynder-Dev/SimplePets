package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntityMagmaCubePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityMagmaCube}
 */
public class EntityMagmaCubePet extends EntitySlimePet implements IEntityMagmaCubePet {
    public EntityMagmaCubePet(PetType type, PetUser user) {
        super(EntityTypes.MAGMA_CUBE, type, user);
    }
}
