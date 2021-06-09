package simplepets.brainsynder.versions.v1_17_R1.entity.branch;

import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

public abstract class EntityIllagerAbstractPet extends EntityRaiderPet {
    public EntityIllagerAbstractPet(EntityTypes<? extends EntityInsentient> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }
}