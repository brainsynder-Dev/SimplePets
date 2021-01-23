package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntityGiantPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;

public class EntityGiantPet extends EntityPet implements IEntityGiantPet {
    public EntityGiantPet(PetType type, PetUser user) {
        super(EntityTypes.GIANT, type, user);
    }
}
