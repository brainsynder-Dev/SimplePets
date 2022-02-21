package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityEvokerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.branch.EntityIllagerWizardPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityEvoker}
 */
public class EntityEvokerPet extends EntityIllagerWizardPet implements IEntityEvokerPet {
    public EntityEvokerPet(PetType type, PetUser user) {
        super(EntityType.EVOKER, type, user);
    }
}
