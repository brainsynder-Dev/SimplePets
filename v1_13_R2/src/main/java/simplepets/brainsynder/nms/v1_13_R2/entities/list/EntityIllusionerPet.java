package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityIllusionerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.branch.EntityIllagerWizardPet;

/**
 * NMS: {@link net.minecraft.server.v1_13_R2.EntityIllagerIllusioner}
 */
@Size(width = 0.6F, length = 1.95F)
public class EntityIllusionerPet extends EntityIllagerWizardPet implements IEntityIllusionerPet {
    public EntityIllusionerPet(EntityTypes<?> type, World world) {
        super(type, world);
    }
    public EntityIllusionerPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }
}
