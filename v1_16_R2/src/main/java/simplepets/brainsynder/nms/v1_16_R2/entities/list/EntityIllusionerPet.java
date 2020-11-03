package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import net.minecraft.server.v1_16_R2.EntityTypes;
import net.minecraft.server.v1_16_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityIllusionerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.branch.EntityIllagerWizardPet;


/**
 * NMS: {@link net.minecraft.server.v1_16_R2.EntityIllagerIllusioner}
 */
@Size(width = 0.6F, length = 1.95F)
public class EntityIllusionerPet extends EntityIllagerWizardPet implements IEntityIllusionerPet {
    public EntityIllusionerPet(World world) {
        super(EntityTypes.ILLUSIONER, world);
    }
    public EntityIllusionerPet(World world, IPet pet) {
        super(EntityTypes.ILLUSIONER, world, pet);
    }
}
