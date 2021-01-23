package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntityEvokerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.branch.EntityIllagerWizardPet;

public class EntityEvokerPet extends EntityIllagerWizardPet implements IEntityEvokerPet {
    public EntityEvokerPet(PetType type, PetUser user) {
        super(EntityTypes.EVOKER, type, user);
    }
}
