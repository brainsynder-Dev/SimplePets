package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntitySquidPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;

public class EntitySquidPet extends EntityPet implements IEntitySquidPet {
    public EntitySquidPet(PetType type, PetUser user) {
        super(EntityTypes.SQUID, type, user);
    }
}
