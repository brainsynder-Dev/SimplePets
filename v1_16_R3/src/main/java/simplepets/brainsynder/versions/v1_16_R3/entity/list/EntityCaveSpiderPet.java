package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntityCaveSpiderPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;

public class EntityCaveSpiderPet extends EntityPet implements IEntityCaveSpiderPet {
    public EntityCaveSpiderPet(PetType type, PetUser user) {
        super(EntityTypes.CAVE_SPIDER, type, user);
    }
}
