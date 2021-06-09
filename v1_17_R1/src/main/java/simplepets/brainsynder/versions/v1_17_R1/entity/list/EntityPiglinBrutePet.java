package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntityPiglinBrutePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.branch.EntityPiglinAbstractPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityPiglinBrute}
 */
public class EntityPiglinBrutePet extends EntityPiglinAbstractPet implements IEntityPiglinBrutePet {
    public EntityPiglinBrutePet(PetType type, PetUser user) {
        super(EntityTypes.PIGLIN_BRUTE, type, user);
    }
}
