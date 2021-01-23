package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntityOcelotPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityTameablePet;

public class EntityOcelotPet extends EntityTameablePet implements IEntityOcelotPet {
    public EntityOcelotPet(PetType type, PetUser user) {
        super(EntityTypes.OCELOT, type, user);
    }
}
