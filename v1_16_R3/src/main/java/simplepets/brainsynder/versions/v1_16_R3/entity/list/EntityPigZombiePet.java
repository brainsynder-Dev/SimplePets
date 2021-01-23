package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntityPigZombiePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

public class EntityPigZombiePet extends EntityZombiePet implements IEntityPigZombiePet {
    public EntityPigZombiePet(PetType type, PetUser user) {
        super(EntityTypes.ZOMBIFIED_PIGLIN, type, user);
    }
}
