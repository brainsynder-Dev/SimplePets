package simplepets.brainsynder.nms.v1_16_R3.entities.list;

import net.minecraft.server.v1_16_R3.EntityCreature;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityEvokerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R3.entities.branch.EntityIllagerWizardPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityEvoker}
 */
@Size(width = 0.6F, length = 1.95F)
public class EntityEvokerPet extends EntityIllagerWizardPet implements IEntityEvokerPet {
    public EntityEvokerPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
    public EntityEvokerPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
}
