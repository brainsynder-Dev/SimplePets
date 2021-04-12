package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntityMulePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.branch.EntityDonkeyAbstractPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityHorseMule}
 */
public class EntityMulePet extends EntityDonkeyAbstractPet implements IEntityMulePet {
    public EntityMulePet(PetType type, PetUser user) {
        super(EntityTypes.MULE, type, user);
    }
}
