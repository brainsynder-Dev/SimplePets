package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityEvokerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.branch.EntityIllagerWizardPet;

/**
 * NMS: {@link net.minecraft.server.v1_13_R2.EntityEvoker}
 */
@Size(width = 0.6F, length = 1.95F)
public class EntityEvokerPet extends EntityIllagerWizardPet implements IEntityEvokerPet {
    public EntityEvokerPet(EntityTypes<?> type, World world) {
        super(type, world);
    }
    public EntityEvokerPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }
}
