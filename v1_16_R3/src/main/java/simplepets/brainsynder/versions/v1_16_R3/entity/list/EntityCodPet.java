package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntityCodPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityFishPet;

public class EntityCodPet extends EntityFishPet implements IEntityCodPet {
    public EntityCodPet(PetType type, PetUser user) {
        super(EntityTypes.COD, type, user);
    }
}
